package com.grpc.card.repository;

import com.grpc.card.document.Card;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CardRepository extends MongoRepository<Card, String> {

    List<Card> findAllByUserId(String userId);
}
