package com.microservice.admin.mapper.mapstruct;


import com.microservice.admin.dto.request.UserRequest;
import com.microservice.admin.dto.response.UserResponse;
import com.microservice.admin.entitys.Users;
import com.microservice.core.admin.constant.export.ExportUserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users toUser(UserRequest userRequest);
    UserResponse toDto(Users users);
}
