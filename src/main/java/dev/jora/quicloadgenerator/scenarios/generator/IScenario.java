package dev.jora.quicloadgenerator.scenarios.generator;

import dev.jora.quicloadgenerator.models.CommonRequest;

public interface IScenario {
    CommonRequest nextRequest();
}
