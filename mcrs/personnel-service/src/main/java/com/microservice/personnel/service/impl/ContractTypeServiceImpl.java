package com.microservice.personnel.service.impl;

import com.microservice.core.constant.ApiResponse;
import com.microservice.core.constant.Message;
import com.microservice.core.exception.ApplicationException;
import com.microservice.core.exception.ErrorCode;
import com.microservice.personnel.dto.request.ContractTypeRequest;
import com.microservice.personnel.entity.ContractType;
import com.microservice.personnel.mapper.ContractTypeMapper;
import com.microservice.personnel.repo.ContractTypeRepo;
import com.microservice.personnel.service.ContractTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContractTypeServiceImpl implements ContractTypeService {

    private final ContractTypeMapper contractTypeMapper;
    private final ContractTypeRepo contractTypeRepo;
    @Override
    @Transactional
    public ApiResponse create(ContractTypeRequest request) {
        ContractType model = contractTypeMapper.toEntity(request);
        if(Objects.isNull(model)){
            throw new ApplicationException(ErrorCode.OBJECT_IS_NULL);
        } else {
            if(Objects.isNull(request.getCodeType()) && Objects.isNull(request.getTypeName())){
                throw new ApplicationException(ErrorCode.INPUT_ERROR);
            }
            if(request.getCodeType() != null){
                boolean kt = contractTypeRepo.existsById(request.getCodeType());
                if(kt){
                    return ApiResponse.builder()
                            .code(HttpStatus.OK.value())
                            .message(Message.UPDATE_SUCCESS)
                            .data(contractTypeRepo.save(model))
                            .build();
                }
                throw new ApplicationException(ErrorCode.OBJECT_NOT);
            }
            return ApiResponse.builder()
                    .code(HttpStatus.OK.value())
                    .message(Message.CREATE_SUCCESS)
                    .data(contractTypeMapper.toDto(contractTypeRepo.save(model)))
                    .build();
        }
    }
}
