package com.microservice.admin.service;


import com.microservice.admin.dto.request.PermissionRequest;
import org.springframework.data.domain.Pageable;

public interface PermissionService {
    Object create(PermissionRequest permissionRequest);

    Object find(Pageable pageable);

    Object delete(String name);
}
