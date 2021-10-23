package com.grpc.card.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("grpc")
public class GrpcProperties {

    private int port;

    private String host;
}