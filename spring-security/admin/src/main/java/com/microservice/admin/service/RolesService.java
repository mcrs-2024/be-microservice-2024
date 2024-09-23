package com.microservice.admin.service;


import com.microservice.admin.dto.request.RolesRequest;
import com.microservice.admin.entitys.Roles;
import org.springframework.data.domain.Pageable;

public interface RolesService {
	Roles findByDescription(String description);

	Object create(RolesRequest rolesRequest);

	Object find(Pageable pageable);

	Object delete(String name);
}
