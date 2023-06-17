package com.grpc.user.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("services")
public class ServicesProperties {

    private GrpcProperties card;
}
