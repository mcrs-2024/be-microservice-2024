package com.microservice.admin.controller;

import com.microservice.admin.configuration.ConfigJwt;
import com.microservice.admin.dto.request.SearchUserRequest;
import com.microservice.admin.dto.request.UserRequest;
import com.microservice.admin.entitys.Users;
import com.microservice.admin.service.UsersService;
import com.microservice.core.constant.ApiResponse;
import com.microservice.core.admin.constant.ApiUrl;
import com.microservice.core.constant.Message;
import com.microservice.core.constant.PageableRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;

@RequestMapping(ApiUrl.URL_ADMIN)
@RestController
@Log4j2
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class User {


    private final UsersService usersService;

    private final ConfigJwt token;


    @PostMapping("/find")
    public ApiResponse find(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestBody SearchUserRequest searchUserRequest
            ){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(x -> log.info(x.getAuthority()));
        return ApiResponse.builder().code(HttpStatus.OK.value()).data(usersService.findAll(
                PageableRequest.formatPage(page, limit), searchUserRequest
        )).message("Success").build();
    }

    @PostMapping("/find/one")
    public ApiResponse findOne(){
        return ApiResponse.builder().code(HttpStatus.OK.value()).data(token.userRequest()).message(Message.SUCCESS_QUERY).build();
    }

    @PostMapping("/create")
    public ApiResponse create(@RequestBody Users request) throws SQLException {
        return ApiResponse.builder().code(HttpStatus.OK.value()).data(usersService.save(request)).message(Message.CREATE_SUCCESS).build();
    }

    @PostMapping("/update")
    public ApiResponse update(@RequestBody UserRequest request) throws SQLException {
        return ApiResponse.builder().code(HttpStatus.OK.value()).data(usersService.updateUsers(request)).message(Message.UPDATE_SUCCESS).build();
    }

}
