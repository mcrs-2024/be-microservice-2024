package com.microservice.admin.keycloak.controller;


import com.microservice.admin.dto.request.RegistrationRequest;
import com.microservice.admin.keycloak.service.AdminService;
import com.microservice.core.constant.ApiResponse;
import com.microservice.core.constant.Message;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/keycloak")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RegisterKeycloak {

    private final AdminService adminService;

    @PostMapping("/register")
    ApiResponse register(@RequestBody @Valid RegistrationRequest request){
        return ApiResponse.builder().message(Message.REGISTER)
                .code(HttpStatus.OK.value())
                .data(adminService.register(request)).build();
    }

    @PostMapping("/profiles")
    ApiResponse profiles(){
        return ApiResponse.builder().message(Message.SUCCESS_QUERY)
                .code(HttpStatus.OK.value())
                .data(adminService.findProfiles()).build();
    }

}
