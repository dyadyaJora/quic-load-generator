package dev.jora.quicloadgenerator.controllers;

import dev.jora.quicloadgenerator.models.CommonResponse;
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
        CommonResponse response = requestRunnable.runQuicRequest();
        Assert.assertNotNull(response);
    }
}
