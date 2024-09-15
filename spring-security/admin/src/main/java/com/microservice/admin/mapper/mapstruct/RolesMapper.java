package com.microservice.admin.mapper.mapstruct;

import com.microservice.admin.dto.request.RolesRequest;
import com.microservice.admin.dto.response.RolesResponse;
import com.microservice.admin.entitys.Roles;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RolesMapper {

    /**
     * @Mapping(target = "id", ignore = true)
     * bỏ qua trường này trong enity không map (tự định nghĩa)
     * @param rolesRequest
     * @return
     */
    @Mapping(target = "permission", ignore = true)
    Roles toEntity(RolesRequest rolesRequest);
    RolesResponse toDto(Roles roles);
}
