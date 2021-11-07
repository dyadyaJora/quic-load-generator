package dev.jora.quicloadgenerator.controllers;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;

public class RequestRunnableTest {

    //"QUIC_VERSION=draft-29"
    @Test
    public void testSimpleQuicRequest() throws IOException, InterruptedException {
        URI serverUri = URI.create("https://kubernetes.docker.internal:6121/");
        RequestRunnable requestRunnable = new RequestRunnable(serverUri, true);
        HttpResponse<String> response = requestRunnable.runQuicRequest();
        Assert.assertNotNull(response);
    }
}
