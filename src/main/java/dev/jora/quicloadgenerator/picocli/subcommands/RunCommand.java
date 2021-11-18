package dev.jora.quicloadgenerator.picocli.subcommands;

import dev.jora.quicloadgenerator.controllers.RateLimiterService;
import dev.jora.quicloadgenerator.controllers.RateLimiterServiceImpl;
import dev.jora.quicloadgenerator.models.CommonResponse;
import dev.jora.quicloadgenerator.models.ScenarioOptions;
import dev.jora.quicloadgenerator.scenarios.IScenario;
import dev.jora.quicloadgenerator.scenarios.QuicGetScenario;
import picocli.CommandLine.*;

import java.net.URI;
import java.util.concurrent.Callable;

@Command(name = "run", description = "Start execution process")
public class RunCommand implements Runnable {
    @Option(names = { "--rps", "-r" }, required = true, description = "requests per second", paramLabel = "RATE")
    Integer rps;

    @Option(names = {"-s", "--seconds"}, description = "Experiment duration in seconds", paramLabel = "SECONDS")
    Integer seconds;

    @Option(names = {"-c", "--count"}, description = "Total requests count in experiment", paramLabel = "REQUESTS_COUNT")
    Integer requestsCount;

    @Option(names = {"-k", "--insecure"}, description = "Disable certificate verification")
    boolean disableCertificateVerification;

    @Option(names = {"-t", "--scenario-type"}, required = false, defaultValue = "quic-get", description = "Scenario type")
    String scenarioType;

    @Parameters
    URI serverUri;

    @Override
    public void run() {
        if ((seconds == null) == (requestsCount == null)) {
            System.out.println("One of SECONDS or REQUESTS_COUNT should be defined to start evaluation!");
            return;
        }

        RateLimiterService service = RateLimiterServiceImpl.instance(rps);
        if (service == null) {
            return;
        }

        ScenarioOptions options = new ScenarioOptions();
        options.setServerUri(serverUri);
        options.setDisableCertificateVerification(disableCertificateVerification);

        Callable<CommonResponse> callable = getScenarioByType(scenarioType, options);
        if (callable == null) {
            System.out.println("Error! Unknown scenario type: " + scenarioType);
            return;
        }

        try {
            if (seconds == null) {
                service.runByCount(callable, requestsCount);
            } else {
                service.runBySeconds(callable, seconds);
            }
            System.out.println("Started successfully!");
        } catch (Exception err) {
            System.out.println("Error");
            err.printStackTrace();
        }
    }

    private static IScenario getScenarioByType(String type, ScenarioOptions options) {
        System.out.println("Scenario selection for type " + type);
        switch (type) {
            case "quic-get": return QuicGetScenario.instance(options);
            default: return null;
        }
    }
}
