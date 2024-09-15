package com.microservice.admin.controller;

import com.microservice.admin.dto.request.PermissionRequest;
import com.microservice.admin.service.PermissionService;
import com.microservice.core.constant.ApiResponse;
import com.microservice.core.admin.constant.ApiUrl;
import com.microservice.core.constant.Message;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.URL_PERMISSION)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Permission {

    private final PermissionService permissionService;


    @PostMapping("/create")
    public ApiResponse create(@RequestBody PermissionRequest permissionRequest){
        return ApiResponse
                .builder()
                .code(200)
                .message(Message.CREATE_SUCCESS)
                .data(permissionService.create(permissionRequest))
                .build();
    }

    @PostMapping("/find")
    public ApiResponse find(
            @RequestParam(value = "page",defaultValue = "0") Integer page,
            @RequestParam(value = "limit",defaultValue = "10") Integer limit
    ){
        return ApiResponse
                .builder()
                .code(HttpStatus.OK.value())
                .message(Message.SUCCESS_QUERY)
                .data(permissionService.find(page,limit))
                .build();
    }

    @PostMapping("/delete")
    public ApiResponse delete(@RequestParam(value = "name", required = true) String name){
        return ApiResponse
                .builder()
                .code(HttpStatus.OK.value())
                .message(Message.DELETE_SUCCESS)
                .data(permissionService.delete(name))
                .build();
    }

}
