package com.grpc.user.mapper;

import com.grpc.user.document.User;
import com.grpc.user.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserDto toDto(User user);

    User toDocument(UserDto userDto);
}
