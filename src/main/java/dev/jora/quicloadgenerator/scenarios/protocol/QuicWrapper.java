package dev.jora.quicloadgenerator.scenarios.protocol;

import dev.jora.quicloadgenerator.scenarios.generator.BaseScenario;
import net.luminis.http3.Http3ClientBuilder;

import java.net.http.HttpClient;

public class QuicWrapper extends BaseProtocolWrapper {
    public QuicWrapper(BaseScenario scenario) {
        super(scenario);
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
