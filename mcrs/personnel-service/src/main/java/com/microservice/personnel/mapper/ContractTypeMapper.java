package com.microservice.personnel.mapper;

import com.microservice.personnel.dto.request.ContractTypeRequest;
import com.microservice.personnel.dto.response.ContractTypeResponse;
import com.microservice.personnel.entity.ContractType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractTypeMapper {

    private final ModelMapper modelMapper;

    public ContractType toEntity(ContractTypeRequest request) {
        return modelMapper.map(request, ContractType.class);
    }

    public ContractTypeResponse toDto(ContractType entity) {
        return modelMapper.map(entity, ContractTypeResponse.class);
    }
}
