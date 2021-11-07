package dev.jora.quicloadgenerator.controllers;

import net.luminis.http3.Http3Client;

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

    public RequestRunnable() {

    }

    @Override
    public void run() {
        try {
            this.runQuicRequest();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void runQuicRequest()  throws IOException, InterruptedException{
        URI serverUrl = URI.create("https://127.0.0.1:6121/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(serverUrl)
                .header("User-Agent", "Flupke http3 library")
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpClient client = Http3Client.newHttpClient();
        long start = System.currentTimeMillis();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        long end = System.currentTimeMillis();
        System.out.println("Got HTTP response " + httpResponse);
        System.out.println("-   HTTP headers: " + httpResponse.headers());
        long downloadSpeed = httpResponse.body().length() / (end - start);
        System.out.println("-   HTTP body (" + httpResponse.body().length() + " bytes, " + downloadSpeed + " B/s):");
        System.out.println(httpResponse.body());
    }
}
