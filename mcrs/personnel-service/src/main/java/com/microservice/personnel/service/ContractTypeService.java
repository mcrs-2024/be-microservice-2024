package com.microservice.personnel.service;

import com.microservice.core.constant.ApiResponse;
import com.microservice.personnel.dto.request.ContractTypeRequest;

public interface ContractTypeService {
    ApiResponse create(ContractTypeRequest request);
}
