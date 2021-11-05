package dev.jora.quicloadgenerator.picocli.subcommands;

import picocli.CommandLine.*;

@Command(name = "run", description = "Start execution process")
public class RunCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Started successfully!");
    }
}
