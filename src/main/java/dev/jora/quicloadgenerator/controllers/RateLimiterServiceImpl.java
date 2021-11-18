package dev.jora.quicloadgenerator.controllers;

import dev.jora.quicloadgenerator.models.CommonResponse;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;

public class RateLimiterServiceImpl implements RateLimiterService {
    private final Bucket bucket;
    private final ExecutorService executor;
    private final CompletionService<CommonResponse> service;
    private final ExecutorService ioExecutor;
    private final CompletionService<String> ioService;
    private final int rps;

    private RateLimiterServiceImpl(int rps) {
        this.bucket = createBucket(rps);
        this.rps = rps;
        this.executor = Executors.newFixedThreadPool(rps);
        this.service = new ExecutorCompletionService<>(this.executor);

        this.ioExecutor = Executors.newSingleThreadExecutor();
        this.ioService = new ExecutorCompletionService<>(this.ioExecutor);
    }

    public static RateLimiterService instance(int rps) {
        if (rps < 0 || rps > 1000) {
            System.out.println("Invalid RPS value! Stopping...");
            return null;
        }

        return new RateLimiterServiceImpl(rps);
    }

    public Bucket createBucket(int rps) {
        Bandwidth limit = Bandwidth.simple(rps, Duration.ofSeconds(1));
        return Bucket4j.builder().addLimit(limit).build();
    }

    // @TODO: Or would Supplier<Callable> be better?
    @Override
    public void runByCount(Callable<CommonResponse> callable, int count) throws InterruptedException {
        Thread waiter = new Thread(() -> {
            System.out.println("waiter started! " + Instant.now().toString());
            try {
                while (!executor.isTerminated()) {
                    System.out.println("here" + Instant.now().toString());
                    final Future<CommonResponse> future = service.take();
                    String resultString = " RESULT === " + future.get().toString();
                    System.out.println(resultString);
                    ioService.submit(() -> resultString);
                }
            } catch (InterruptedException | ExecutionException err) {
                err.printStackTrace();
            }
        });
        Thread ioWaiter = new Thread(() -> {

            File file = new File("./tmp.txt");
            try {

                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write("".getBytes());
                }
//            try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("tmp.txt", true))) {
                while (!ioExecutor.isTerminated()) {
                    final Future<String> future = ioService.take();
                    String resultString = future.get() + "\n";
//                    bufferedWriter.write(resultString + "\n");
                    Files.write(file.toPath(), resultString.getBytes(), StandardOpenOption.APPEND);
                }
            } catch (InterruptedException | ExecutionException | IOException err) {
                err.printStackTrace();
            }
        });
        waiter.start();
        ioWaiter.start();

        for (int i = 0; i < count; i++) {
            bucket.asScheduler().consume(1);
            service.submit(callable);
        }

        executor.shutdown();
        waiter.join();

        ioExecutor.shutdown();
        ioWaiter.join();
    }

    @Override
    public void runBySeconds(Callable<CommonResponse> callable, int seconds) throws InterruptedException {
        int count = seconds * this.rps;
        this.runByCount(callable, count);
    }
}
