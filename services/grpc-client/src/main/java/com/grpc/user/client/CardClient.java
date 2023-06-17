package com.grpc.user.client;

import com.grpc.card.*;
import com.grpc.user.document.User;
import com.grpc.user.dto.UserCardDto;
import com.grpc.user.property.ServicesProperties;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardClient {

    private final ServicesProperties servicesProps;

    public List<UserCardDto> getUserCards(User user) {
        var channel = openChannel();
        try {
            var stub = getBlockingStub(channel);
            var getCardResponse = stub.getCardsByUser(GetCardRequest.newBuilder()
                    .setUserId(user.getId())
                    .build());
            return getUserCards(getCardResponse.getCardsList());
        } finally {
            channel.shutdown();
        }
    }

    public List<UserCardDto> addCard(String userId, List<UserCardDto> userCards) {
        var channel = openChannel();
        try {
            var stub = getBlockingStub(channel);
            var addCardResponse = stub.addCard(AddCardRequest.newBuilder()
                    .setUserId(userId)
                    .addAllCard(userCards.stream()
                            .map(userCard -> Card.newBuilder()
                                    .setHolderName(userCard.getHolderName())
                                    .setNumber(userCard.getNumber())
                                    .setBalance(userCard.getBalance())
                                    .setExpiredAt(userCard.getExpiredAt())
                                    .putAllSpecifications(userCard.getSpecifications())
                                    .setStatus(Status.valueOf(userCard.getStatus()))
                                    .setType(Type.valueOf(userCard.getType()))
                                    .addAllCurrencies(userCard.getCurrencies().stream()
                                            .map(Currency::valueOf)
                                            .collect(Collectors.toList()))
                                    .build())
                            .collect(Collectors.toList()))
                    .build());
            return getUserCards(addCardResponse.getCardList());
        } finally {
            channel.shutdown();
        }
    }

    private List<UserCardDto> getUserCards(List<Card> cards) {
        return cards.stream()
                .map(card -> UserCardDto.builder()
                        .id(card.getId())
                        .holderName(card.getHolderName())
                        .number(card.getNumber())
                        .balance(card.getBalance())
                        .expiredAt(card.getExpiredAt())
                        .status(card.getStatus().name())
                        .type(card.getType().name())
                        .specifications(card.getSpecificationsMap())
                        .currencies(card.getCurrenciesList()
                                .stream()
                                .map(Currency::name)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    private ManagedChannel openChannel() {
        var cardService = servicesProps.getCard();
        return ManagedChannelBuilder.forAddress(cardService.getHost(), cardService.getPort())
                .usePlaintext()
                .build();
    }

    private CardServiceGrpc.CardServiceBlockingStub getBlockingStub(ManagedChannel channel) {
        return CardServiceGrpc.newBlockingStub(channel);
    }
}
