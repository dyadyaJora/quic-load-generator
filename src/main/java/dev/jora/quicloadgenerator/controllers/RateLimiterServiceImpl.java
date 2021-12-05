package dev.jora.quicloadgenerator.controllers;

import com.opencsv.CSVWriter;
import dev.jora.quicloadgenerator.models.CommonResponse;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.*;

public class RateLimiterServiceImpl implements RateLimiterService {
    private final Bucket bucket;
    private final ExecutorService executor;
    private final CompletionService<CommonResponse> service;
    private final ExecutorService ioExecutor;
    private final CompletionService<String[]> ioService;
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
    public void runByCount(Callable<CommonResponse> callable, int count, File outFile) throws InterruptedException {
        Thread waiter = new Thread(() -> {
            System.out.println("waiter started! " + Instant.now().toString());
            try {
                int counter = 0;
                while (!executor.isTerminated() && counter++ < count) {
                    System.out.println("here" + Instant.now().toString());

                    final Future<CommonResponse> future = service.take();
                    String[] resultStrings = future.get().toCsvLine();
                    System.out.println(Arrays.toString(resultStrings));
                    ioService.submit(() -> resultStrings);
                }
            } catch (InterruptedException | ExecutionException err) {
                err.printStackTrace();
            }
        });

        Thread ioWaiter = new Thread(() -> {
            try {
                try (FileOutputStream fos = new FileOutputStream(outFile)) {
                    fos.write("".getBytes());
                }
                try (CSVWriter csvWriter = new CSVWriter(new FileWriter(outFile, true))) {
                    int counter = 0;
                    while (!ioExecutor.isTerminated() && counter++ < count) {
                        final Future<String[]> future = ioService.take();
                        csvWriter.writeNext(future.get());
                        csvWriter.flush();
                    }
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
    public void runBySeconds(Callable<CommonResponse> callable, int seconds, File outFile) throws InterruptedException {
        int count = seconds * this.rps;
        this.runByCount(callable, count, outFile);
    }
}
