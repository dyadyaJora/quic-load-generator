package dev.jora.quicloadgenerator.scenarios;

import dev.jora.quicloadgenerator.models.CommonRequest;
import dev.jora.quicloadgenerator.models.CommonResponse;

import java.util.concurrent.Callable;

public interface IScenario extends Callable<CommonResponse> {
    CommonRequest nextRequest();
}
