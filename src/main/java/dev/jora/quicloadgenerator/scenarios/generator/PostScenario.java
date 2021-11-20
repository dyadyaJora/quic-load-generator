package dev.jora.quicloadgenerator.scenarios.generator;

import dev.jora.quicloadgenerator.models.CommonRequest;
import dev.jora.quicloadgenerator.models.ScenarioOptions;

public class PostScenario extends BaseScenario {
    public PostScenario(ScenarioOptions options) {
        super(options);
    }

    @Override
    public CommonRequest nextRequest() {
        return null;
    }
}
