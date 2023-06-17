package com.grpc.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCardDto {

    private String id;

    private String holderName;

    private String number;

    private String status;

    private Double balance;

    private String expiredAt;

    private String type;

    private List<String> currencies;

    private Map<String, String> specifications;
}
