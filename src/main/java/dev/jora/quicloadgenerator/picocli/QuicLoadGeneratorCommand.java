package dev.jora.quicloadgenerator.picocli;

import dev.jora.quicloadgenerator.picocli.subcommands.RunCommand;
import picocli.CommandLine.*;

@Command(
        name="quic-load-generator",
        subcommands = {RunCommand.class, HelpCommand.class},
        description = "Generate requests with QUIC traffic"
)
public class QuicLoadGeneratorCommand implements Runnable {
    @Spec Model.CommandSpec spec;

    @Override
    public void run() {
        throw new ParameterException(spec.commandLine(), "Specify a subcommand");
    }
}
