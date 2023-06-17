package com.grpc.card.service;

import com.grpc.card.*;
import com.grpc.card.document.Card;
import com.grpc.card.repository.CardRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService extends CardServiceGrpc.CardServiceImplBase {

    private final CardRepository cardRepository;

    @Override
    public void getCardsByUser(GetCardRequest request, StreamObserver<GetCardResponse> responseObserver) {
        var userCards = cardRepository.findAllByUserId(request.getUserId())
                .stream()
                .map(this::getCard)
                .collect(Collectors.toList());
        responseObserver.onNext(GetCardResponse.newBuilder()
                .addAllCards(userCards)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void addCard(AddCardRequest request, StreamObserver<AddCardResponse> responseObserver) {
        var cards = request.getCardList()
                .stream()
                .map(card -> getCard(card, request.getUserId()))
                .collect(Collectors.toList());
        cardRepository.saveAll(cards);
        responseObserver.onNext(AddCardResponse.newBuilder()
                .addAllCard(cards.stream()
                        .map(this::getCard)
                        .collect(Collectors.toList()))
                .build());
        responseObserver.onCompleted();
    }

    private Card getCard(com.grpc.card.Card card, String userId) {
        return Card.builder()
                .userId(userId)
                .holderName(card.getHolderName())
                .balance(card.getBalance())
                .number(card.getNumber())
                .expiredAt(card.getExpiredAt())
                .type(card.getType().name())
                .status(card.getStatus().name())
                .currencies(card.getCurrenciesList().stream()
                        .map(Currency::name)
                        .collect(Collectors.toList()))
                .specifications(card.getSpecificationsMap())
                .build();
    }

    private com.grpc.card.Card getCard(Card card) {
        return com.grpc.card.Card.newBuilder()
                .setId(card.getId())
                .setHolderName(card.getHolderName())
                .setBalance(card.getBalance())
                .setNumber(card.getNumber())
                .setExpiredAt(card.getExpiredAt())
                .setType(Type.valueOf(card.getType()))
                .setStatus(Status.valueOf(card.getStatus()))
                .addAllCurrencies(card.getCurrencies().stream()
                        .map(Currency::valueOf)
                        .collect(Collectors.toList()))
                .putAllSpecifications(card.getSpecifications())
                .build();
    }
}