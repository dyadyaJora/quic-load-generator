package dev.jora.quicloadgenerator.controllers;

import dev.jora.quicloadgenerator.models.CommonResponse;

import java.util.concurrent.Callable;

public interface RateLimiterService {
    void runByCount(Callable<CommonResponse> callable, int count) throws InterruptedException;

    void runBySeconds(Callable<CommonResponse> callable, int seconds) throws InterruptedException;
}
