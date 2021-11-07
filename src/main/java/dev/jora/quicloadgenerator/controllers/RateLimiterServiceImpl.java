package dev.jora.quicloadgenerator.controllers;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RateLimiterServiceImpl implements RateLimiterService {
    private final Bucket bucket;
    private final ExecutorService executorService;
    private final int rps;

    private RateLimiterServiceImpl(int rps) {
        this.bucket = createBucket(rps);
        this.rps = rps;
        this.executorService = Executors.newFixedThreadPool(rps);
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


    // @TODO: Or would Supplier<Runnable> be better?
    @Override
    public void runByCount(Runnable runnable, int count) throws InterruptedException {
        for (int i = 0; i < count; i++) {
            bucket.asScheduler().consume(1);
            executorService.submit(runnable);
        }
    }

    @Override
    public void runBySeconds(Runnable runnable, int seconds) throws InterruptedException {
        int count = seconds * this.rps;
        this.runByCount(runnable, count);
    }
}
