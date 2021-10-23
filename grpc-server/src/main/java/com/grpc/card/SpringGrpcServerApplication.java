package com.grpc.card;

import com.grpc.card.property.GrpcProperties;
import com.grpc.card.service.CardService;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;

@SpringBootApplication
@EnableDiscoveryClient
@RequiredArgsConstructor
public class SpringGrpcServerApplication {

    private final CardService cardService;
    private final GrpcProperties grpcProperties;

    public static void main(String[] args) {
        SpringApplication.run(SpringGrpcServerApplication.class, args);
    }

    @EventListener(ContextRefreshedEvent.class)
    public void startServer() throws IOException, InterruptedException {
        var server = ServerBuilder.forPort(grpcProperties.getPort())
                .addService(cardService)
                .build();
        server.start();
        server.awaitTermination();
    }
}