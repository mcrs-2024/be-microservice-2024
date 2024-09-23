package com.microservice.admin.controller;


import com.microservice.admin.dto.request.RolesRequest;
import com.microservice.admin.service.RolesService;
import com.microservice.core.constant.ApiResponse;
import com.microservice.core.admin.constant.ApiUrl;
import com.microservice.core.constant.Message;
import com.microservice.core.constant.PageableRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.URI_ROLES)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Roles {

    private final RolesService rolesService;

    @PostMapping("/create")
    public ApiResponse create(@RequestBody RolesRequest rolesRequest){
        return ApiResponse
                .builder()
                .code(HttpStatus.OK.value())
                .message(Message.CREATE_SUCCESS)
                .data(rolesService.create(rolesRequest))
                .build();
    }

    @PostMapping("/find")
    public ApiResponse find(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "limit",defaultValue = "10") Integer limit
    ){
        return ApiResponse
                .builder()
                .code(HttpStatus.OK.value())
                .message(Message.SUCCESS_QUERY)
                .data(rolesService.find(PageableRequest.formatPage(page,limit)))
                .build();
    }

    @PostMapping("/delete")
    public ApiResponse delete(@RequestParam(value = "id", required = true) String name){
        return ApiResponse
                .builder()
                .code(HttpStatus.OK.value())
                .message(Message.DELETE_SUCCESS)
                .data(rolesService.delete(name))
                .build();
    }

}
