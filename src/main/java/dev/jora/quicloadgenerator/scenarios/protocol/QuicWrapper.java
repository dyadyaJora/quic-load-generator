package dev.jora.quicloadgenerator.scenarios.protocol;

import dev.jora.quicloadgenerator.scenarios.generator.BaseScenario;
import net.luminis.http3.Http3Client;
import net.luminis.http3.Http3ClientBuilder;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class QuicWrapper extends BaseProtocolWrapper {
    public QuicWrapper(BaseScenario scenario) {
        super(scenario);
    }

    protected HttpResponse<String> makeRequest(HttpClient client, HttpRequest request) throws IOException, InterruptedException {
        return ((Http3Client)client).sendWithNewConnection(request, HttpResponse.BodyHandlers.ofString());
    }

    protected void onAfterRequest(HttpClient httpClient) {
        ((Http3Client)httpClient).closeConnection();
    }

    @Override
    protected HttpClient buildHttpClient() {
        Http3ClientBuilder builder = new Http3ClientBuilder();
        if (this.scenario.getOptions().isDisableCertificateVerification()) {
            builder.disableCertificateCheck();
        }

        return builder.build();
    }
}
