package dev.jora.quicloadgenerator;

import dev.jora.quicloadgenerator.picocli.QuicLoadGeneratorCommand;
import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new QuicLoadGeneratorCommand()).execute(args);
        System.exit(exitCode);
    }
}
