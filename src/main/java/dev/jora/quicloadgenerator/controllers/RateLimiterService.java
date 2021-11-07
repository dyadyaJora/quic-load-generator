package dev.jora.quicloadgenerator.controllers;

public interface RateLimiterService {
    void runByCount(Runnable runnable, int count) throws InterruptedException;

    void runBySeconds(Runnable runnable, int seconds) throws InterruptedException;
}
