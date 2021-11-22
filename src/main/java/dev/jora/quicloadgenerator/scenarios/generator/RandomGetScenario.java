package dev.jora.quicloadgenerator.scenarios.generator;

import dev.jora.quicloadgenerator.models.CommonRequest;
import dev.jora.quicloadgenerator.models.ScenarioOptions;
import org.apache.commons.lang3.RandomStringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.time.Duration;

public class RandomGetScenario extends BaseScenario {
    public RandomGetScenario(ScenarioOptions options) {
        super(options);
    }

    @Override
    public CommonRequest nextRequest() {
        URI uri;
        try {
            uri = appendUri(this.options.getServerUri(), "echo", RandomStringUtils.randomAlphabetic(10) + "=" + RandomStringUtils.randomAlphanumeric(5) + "&" + RandomStringUtils.randomAlphabetic(10) + "=" + RandomStringUtils.randomAlphanumeric(5));
        } catch (Exception err) {
            return null;
        }
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofSeconds(10))
                .build();

        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setHttpRequest(httpRequest);
        return commonRequest;
    }

    public static URI appendUri(URI oldUri, String appendPath, String appendQuery) throws URISyntaxException {
        return new URI(oldUri.getScheme(), oldUri.getAuthority(), oldUri.getPath() + appendPath,
                oldUri.getQuery() == null ? appendQuery : oldUri.getQuery() + "&" + appendQuery, oldUri.getFragment());
    }
}
