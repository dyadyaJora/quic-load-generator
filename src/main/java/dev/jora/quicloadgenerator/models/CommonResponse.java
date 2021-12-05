package dev.jora.quicloadgenerator.models;

import lombok.Builder;
import lombok.Data;

import java.net.http.HttpResponse;
import java.time.Instant;

@Data
@Builder
public class CommonResponse {
    private String errorMessage;
    private long durationMs;
    private long bodySize;
    private long speed;
    private Instant startTime;
    private Instant endTime;
    HttpResponse<?> response;

    public String[] toCsvLine() {
        return new String[]{
                this.errorMessage,
                String.valueOf(this.durationMs),
                String.valueOf(this.bodySize),
                String.valueOf(this.speed),
                startTime.toString(),
                endTime.toString(),
                String.valueOf(response.statusCode()),
                response.request().method()
        };
    }
}
