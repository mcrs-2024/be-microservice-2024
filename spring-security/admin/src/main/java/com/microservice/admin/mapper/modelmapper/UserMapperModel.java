package com.microservice.admin.mapper.modelmapper;

import com.microservice.admin.dto.request.RegistrationRequest;
import com.microservice.admin.entitys.Users;
import com.microservice.core.admin.constant.export.ExportUserDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserMapperModel {

    private ModelMapper modelMapper;

    private final BCryptPasswordEncoder bcrytPass = new BCryptPasswordEncoder();

    public List<ExportUserDto> dtoList(List<Users> users){
        return users.stream().map(x -> {
            ExportUserDto dto = new ExportUserDto();
            dto.setUsername(x.getUsername());
            dto.setEmail(x.getEmail());
            dto.setFullname(x.getFullname());
            return dto;
        }).collect(Collectors.toList());
    }

    public Users toEntity(RegistrationRequest request,String userId) {
        return Users.builder()
                .name(userId)
                .username(request.getUsername())
                .fullname(request.getFirstName() + " " + request.getLastName())
                .email(request.getEmail())
                .hashPassword(bcrytPass.encode(request.getPassword()))
                .isDeleted(true)
                .build();
    }

    public RegistrationRequest toDto(Users user){
        return RegistrationRequest.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .dob(user.getCreatedDate().toLocalDateTime().toLocalDate())
                .build();
    }


}
