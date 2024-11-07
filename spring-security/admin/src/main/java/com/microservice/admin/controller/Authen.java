package com.microservice.admin.controller;


import com.microservice.admin.configuration.ConfigJwt;
import com.microservice.admin.dto.request.RefreshRequest;
import com.microservice.admin.entitys.Users;
import com.microservice.admin.service.impl.UserServiceImpl;
import com.microservice.core.admin.constant.dto.request.IntrospectRequest;
import com.microservice.core.admin.constant.dto.response.IntrospectResponse;
import com.microservice.core.constant.ApiResponse;
import com.microservice.core.admin.constant.ApiUrl;
import com.microservice.core.constant.Message;
import com.microservice.core.request.LoginRequest;
import com.microservice.core.request.LogoutRequest;
import com.microservice.core.response.LoginResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.URL_ADMIN)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class Authen {

    @Autowired
    private final AuthenticationManager authenticationManager;

    private final UserServiceImpl customUserDetailsService;

    private final ConfigJwt token;


    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            Users userDetails = customUserDetailsService.users(loginRequest.getUsername());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ApiResponse.builder().data(LoginResponse.builder()
                    .token(token.generateToken(userDetails))
                    .build()).message(Message.LOGIN_SUCCESS).code(HttpStatus.OK.value()).build();
        } catch (Exception e){
            log.info(e);
            return ApiResponse.builder()
                    .message("Login failed: " + e.getMessage())
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .build();
        }
    }

    @PostMapping("/introspect-request")
    public IntrospectResponse introspectRequest(@RequestBody IntrospectRequest request) throws Exception {
        return token.introspectRespponse(request);
    }

    @PostMapping("/logout")
    public ApiResponse logout(@RequestBody LogoutRequest request) throws Exception {
        return ApiResponse.builder().code(HttpStatus.OK.value())
                .data(token.logout(request))
                .message(Message.LOGOUT_SUCCESS).build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse refreshToken(@RequestBody RefreshRequest request) throws Exception {
        return ApiResponse.builder().code(HttpStatus.OK.value())
                .data(token.refreshToken(request))
                .message(Message.REFRESH_SUCCESS).build();
    }
}
