package dev.jora.quicloadgenerator.controllers;

import net.luminis.http3.Http3ClientBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestRunnable implements Runnable {
    private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor();

    private final URI serverUri;
    private boolean disableCertificateVerification;

    public RequestRunnable(URI serverUri, boolean disableCertificateVerification) {
        this.serverUri = serverUri;
        this.disableCertificateVerification = disableCertificateVerification;
    }

    @Override
    public void run() {
        try {
            this.runQuicRequest();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public HttpResponse<String> runQuicRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(this.serverUri)
                .timeout(Duration.ofSeconds(10))
                .build();

        Http3ClientBuilder builder = new Http3ClientBuilder();
        if (this.disableCertificateVerification) {
            builder.disableCertificateCheck();
        }
        HttpClient client = builder.build();
        long start = System.currentTimeMillis();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        long end = System.currentTimeMillis();
        System.out.println("Got HTTP response " + httpResponse);
        System.out.println("-   HTTP headers: " + httpResponse.headers());
        long downloadSpeed = httpResponse.body().length() / (end - start);
        System.out.println("-   HTTP body (" + httpResponse.body().length() + " bytes, " + downloadSpeed + " B/s):");
        System.out.println(httpResponse.body());
        return httpResponse;
    }
}
