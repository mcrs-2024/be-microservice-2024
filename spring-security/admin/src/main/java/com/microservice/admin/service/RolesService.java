package com.microservice.admin.service;


import com.microservice.admin.dto.request.RolesRequest;
import com.microservice.admin.entitys.Roles;

public interface RolesService {
	Roles findByDescription(String description);

	Object create(RolesRequest rolesRequest);

	Object find(Integer page, Integer limit);

	Object delete(String name);
}
