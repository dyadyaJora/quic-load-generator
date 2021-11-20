package dev.jora.quicloadgenerator.scenarios.protocol;

import dev.jora.quicloadgenerator.scenarios.generator.BaseScenario;

import java.net.http.HttpClient;

public class HttpWrapper extends BaseProtocolWrapper {
    public HttpWrapper(BaseScenario scenario) {
        super(scenario);
    }

    @Override
    protected HttpClient buildHttpClient() {
        return HttpClient.newHttpClient();
    }
}
