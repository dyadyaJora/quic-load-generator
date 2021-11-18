package dev.jora.quicloadgenerator.scenarios;

import dev.jora.quicloadgenerator.models.CommonRequest;
import dev.jora.quicloadgenerator.models.CommonResponse;
import dev.jora.quicloadgenerator.models.ScenarioOptions;
import dev.jora.quicloadgenerator.scenarios.protocol.QuicWrapper;

import java.net.http.HttpRequest;
import java.time.Duration;

public class QuicGetScenario extends QuicWrapper {
    private QuicGetScenario(ScenarioOptions options) {
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

    private static QuicGetScenario quicGetScenario = null;
    public static QuicGetScenario instance(ScenarioOptions options) {
        if (quicGetScenario == null) {
            quicGetScenario = new QuicGetScenario(options);
        }

        return quicGetScenario;
    }
}
