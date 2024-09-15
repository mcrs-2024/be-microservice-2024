package com.microservice.personnel.controller;

import com.microservice.core.constant.ApiResponse;
import com.microservice.core.personnel.constant.UrlApi;
import com.microservice.personnel.dto.request.ContractTypeRequest;
import com.microservice.personnel.service.ContractTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(UrlApi.URL)
public class ContractType {

    @Autowired
    private ContractTypeService contractTypeService;

    @PostMapping("/create")
    public ApiResponse create(@RequestBody @Valid ContractTypeRequest request){
        return contractTypeService.create(request);
    }

}
