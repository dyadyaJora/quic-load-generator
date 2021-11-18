package dev.jora.quicloadgenerator.scenarios.protocol;

import dev.jora.quicloadgenerator.models.ScenarioOptions;

import java.net.http.HttpClient;

public abstract class HttpWrapper extends BaseProtocolWrapper {
    public HttpWrapper(ScenarioOptions options) {
        super(options);
    }

    @Override
    protected HttpClient buildHttpClient() {
        return HttpClient.newHttpClient();
    }
}
