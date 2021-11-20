package dev.jora.quicloadgenerator.scenarios.generator;

import dev.jora.quicloadgenerator.models.CommonRequest;
import dev.jora.quicloadgenerator.models.ScenarioOptions;

import java.net.http.HttpRequest;
import java.time.Duration;

public class GetScenario extends BaseScenario {
    public GetScenario(ScenarioOptions options) {
        super(options);
    }

    @Override
    public CommonRequest nextRequest() {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(this.options.getServerUri())
                .timeout(Duration.ofSeconds(10))
                .build();

        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setHttpRequest(httpRequest);
        return commonRequest;
    }
}
