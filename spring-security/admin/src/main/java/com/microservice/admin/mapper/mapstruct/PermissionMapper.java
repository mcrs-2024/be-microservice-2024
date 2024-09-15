package com.microservice.admin.mapper.mapstruct;


import com.microservice.admin.dto.request.PermissionRequest;
import com.microservice.admin.dto.response.PermissionResponse;
import com.microservice.admin.entitys.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toEntity(PermissionRequest permissionRequest);
    PermissionResponse toDto(Permission permission);
}
