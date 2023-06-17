package com.grpc.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringGrpcClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringGrpcClientApplication.class, args);
    }
}
