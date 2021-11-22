package dev.jora.quicloadgenerator.scenarios;

import dev.jora.quicloadgenerator.models.ScenarioOptions;
import dev.jora.quicloadgenerator.scenarios.generator.GetScenario;
import dev.jora.quicloadgenerator.scenarios.generator.BaseScenario;
import dev.jora.quicloadgenerator.scenarios.generator.PostScenario;
import dev.jora.quicloadgenerator.scenarios.generator.RandomGetScenario;
import dev.jora.quicloadgenerator.scenarios.protocol.BaseProtocolWrapper;
import dev.jora.quicloadgenerator.scenarios.protocol.HttpWrapper;
import dev.jora.quicloadgenerator.scenarios.protocol.QuicWrapper;

public class ScenarioFactory {
    public static BaseProtocolWrapper build(
            ProtocolType protocolType,
            ScenarioType scenarioType,
            ScenarioOptions options
    ) {
        BaseProtocolWrapper protocolWrapper;
        BaseScenario scenario;

        switch (scenarioType) {
            case GET: {
                scenario = new GetScenario(options);
                break;
            }
            case POST: {
                scenario = new PostScenario(options);
                break;
            }
            case RANDOM_GET: {
                scenario = new RandomGetScenario(options);
                break;
            }
            case CUSTOM: {
                scenario = null;
                break;
            }
            default: {
                System.out.println("Unknown scenario type selected!");
                return null;
            }
        }

        switch (protocolType) {
            case HTTP: {
                protocolWrapper = new HttpWrapper(scenario);
                break;
            }
            case QUIC: {
                protocolWrapper = new QuicWrapper(scenario);
                break;
            }
            default: {
                System.out.println("Unknown protocol type!");
                return null;
            }
        }

        return protocolWrapper;
    }
}
