package dev.jora.quicloadgenerator.models;

import lombok.Data;

import java.net.URI;

@Data
public class ScenarioOptions {
    private URI serverUri;
    private boolean disableCertificateVerification;
}
