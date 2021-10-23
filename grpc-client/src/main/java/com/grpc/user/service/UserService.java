package com.grpc.user.service;

import com.grpc.user.client.CardClient;
import com.grpc.user.document.User;
import com.grpc.user.dto.UserDto;
import com.grpc.user.mapper.UserMapper;
import com.grpc.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CardClient cardClient;
    private final UserMapper userMapper;

    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    public UserDto addUser(UserDto userDto) {
        var user = userMapper.toDocument(userDto);
        userRepository.save(user);
        var userCards = cardClient.addCard(user.getId(), userDto.getCards());
        userDto.setId(user.getId());
        userDto.setCards(userCards);
        return userDto;
    }

    private UserDto getUser(User user) {
        var userDto = userMapper.toDto(user);
        var userCards = cardClient.getUserCards(user);
        userDto.setCards(userCards);
        return userDto;
    }
}