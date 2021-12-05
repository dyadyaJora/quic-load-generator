package dev.jora.quicloadgenerator.controllers;

import dev.jora.quicloadgenerator.models.CommonResponse;

import java.io.File;
import java.util.concurrent.Callable;

public interface RateLimiterService {
    void runByCount(Callable<CommonResponse> callable, int count, File outFile) throws InterruptedException;

    void runBySeconds(Callable<CommonResponse> callable, int seconds, File outFile) throws InterruptedException;
}
