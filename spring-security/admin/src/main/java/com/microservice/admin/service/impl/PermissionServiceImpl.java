package com.microservice.admin.service.impl;


import com.microservice.admin.dto.request.PermissionRequest;
import com.microservice.admin.entitys.Permission;
import com.microservice.admin.mapper.mapstruct.PermissionMappers;
import com.microservice.admin.repository.PermissionRepo;
import com.microservice.admin.service.PermissionService;
import com.microservice.core.constant.Message;
import com.microservice.core.constant.PageListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepo permissionRepo;

    private final PermissionMappers permissionMapper;
    @Override
    public Object create(PermissionRequest permissionRequest) {
        Permission permission = permissionMapper.toEntity(permissionRequest);
        return permissionMapper.toDto(permissionRepo.saveAndFlush(permission));
    }

    @Override
    public Object find(Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page,limit);
        Page<Permission> pages = permissionRepo.findAll(pageable);
        if(pages.isEmpty()){
            return null;
        }
        return new PageListResponse<>(pages.getNumber(), pages.getTotalElements(), pages.getSize(), pages.getContent().stream()
                .map(permissionMapper::toDto).toList());
    }

    @Override
    public Object delete(String name) {
        permissionRepo.deleteById(name);
        return Message.DELETE_SUCCESS;
    }
}
