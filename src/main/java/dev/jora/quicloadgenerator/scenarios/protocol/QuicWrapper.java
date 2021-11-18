package dev.jora.quicloadgenerator.scenarios.protocol;

import dev.jora.quicloadgenerator.models.ScenarioOptions;
import net.luminis.http3.Http3ClientBuilder;

import java.net.http.HttpClient;

public abstract class QuicWrapper extends BaseProtocolWrapper {
    public QuicWrapper(ScenarioOptions options) {
        super(options);
    }

    @Override
    protected HttpClient buildHttpClient() {
        Http3ClientBuilder builder = new Http3ClientBuilder();
        if (this.options.isDisableCertificateVerification()) {
            builder.disableCertificateCheck();
        }

        return builder.build();
    }
}
