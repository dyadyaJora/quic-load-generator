package dev.jora.quicloadgenerator.picocli.subcommands;

import dev.jora.quicloadgenerator.controllers.RateLimiterService;
import dev.jora.quicloadgenerator.controllers.RateLimiterServiceImpl;
import dev.jora.quicloadgenerator.controllers.RequestRunnable;
import picocli.CommandLine.*;

@Command(name = "run", description = "Start execution process")
public class RunCommand implements Runnable {
    @Option(names = { "--rps", "-r" }, required = true, description = "requests per second", paramLabel = "RATE")
    Integer rps;

    @Option(names = {"-s", "--seconds"}, description = "Experiment duration in seconds", paramLabel = "SECONDS")
    Integer seconds;

    @Option(names = {"-c", "--count"}, description = "Total requests count in experiment", paramLabel = "REQUESTS_COUNT")
    Integer requestsCount;

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

        try {
            service.runByCount(new RequestRunnable(), 1);
            System.out.println("Started successfully!");
        } catch (Exception err) {
            System.out.println("Error");
            err.printStackTrace();
        }
    }
}
