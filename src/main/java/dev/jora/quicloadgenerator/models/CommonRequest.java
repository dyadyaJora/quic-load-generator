package dev.jora.quicloadgenerator.models;

import lombok.Data;

import java.net.http.HttpRequest;

@Data
public class CommonRequest {
    private HttpRequest httpRequest;
}
