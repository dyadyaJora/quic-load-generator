package dev.jora.quicloadgenerator.scenarios.generator;

import dev.jora.quicloadgenerator.models.ScenarioOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class BaseScenario implements IScenario {
    @Getter
    public ScenarioOptions options;
}
