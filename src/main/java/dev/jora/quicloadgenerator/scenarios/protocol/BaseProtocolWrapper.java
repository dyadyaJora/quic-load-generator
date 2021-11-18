package dev.jora.quicloadgenerator.scenarios.protocol;

import dev.jora.quicloadgenerator.models.CommonResponse;
import dev.jora.quicloadgenerator.models.ScenarioOptions;
import dev.jora.quicloadgenerator.scenarios.IScenario;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class BaseProtocolWrapper implements IScenario {
    protected ScenarioOptions options;

    public BaseProtocolWrapper(ScenarioOptions options) {
        this.options = options;
    }

    protected abstract HttpClient buildHttpClient();

    @Override
    public CommonResponse call() {
        try {
            return this.runRequest();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return null;
    }

    public CommonResponse runRequest() throws IOException, InterruptedException {
        HttpRequest request = this.nextRequest().getHttpRequest();
        HttpClient client = this.buildHttpClient();

        long start = System.currentTimeMillis();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        long end = System.currentTimeMillis();
        System.out.println("Got HTTP response " + httpResponse);
        System.out.println("-   HTTP headers: " + httpResponse.headers());
        long downloadSpeed = httpResponse.body().length() / (end - start);
        System.out.println("-   HTTP body (" + httpResponse.body().length() + " bytes, " + downloadSpeed + " B/s):");
        System.out.println(httpResponse.body());

        return CommonResponse.builder()
                .response(httpResponse)
                .build();
    }
}
