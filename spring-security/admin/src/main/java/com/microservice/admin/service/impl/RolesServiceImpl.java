package com.microservice.admin.service.impl;


import com.microservice.admin.dto.request.PermissionRequest;
import com.microservice.admin.dto.request.RolesRequest;
import com.microservice.admin.entitys.Roles;
import com.microservice.admin.mapper.mapstruct.RolesMapper;
import com.microservice.admin.repository.PermissionRepo;
import com.microservice.admin.repository.RolesRepo;
import com.microservice.admin.service.RolesService;
import com.microservice.core.constant.Message;
import com.microservice.core.constant.PageListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements RolesService {
	

    private final RolesRepo rolesRepo;

	private final RolesMapper rolesMapper;

	private final PermissionRepo permissionRepo;
	
	@Override
	public Roles findByDescription(String description) {
		return rolesRepo.findByDescription(description);
	}

	@Override
	public Object create(RolesRequest rolesRequest) {
		Roles roles = rolesMapper.toEntity(rolesRequest);
		for(PermissionRequest s : rolesRequest.getPermission()){
			var permission = permissionRepo.findAllById(Collections.singleton(s.getName()));
			roles.setPermission(new HashSet<>(permission));
			roles = rolesRepo.save(roles);
		}
		return rolesMapper.toDto(roles);
	}


	@Override
	public Object find(Pageable pageable) {
		Page<Roles> pages = rolesRepo.findAll(pageable);
		if(pages.isEmpty()){
			return null;
		}
		return new PageListResponse<>(pages.getNumber(),pages.getTotalElements(),pages.getSize(),pages.getContent().stream().
				map(rolesMapper::toDto).toList());
	}

	@Override
	public Object delete(String name) {
		rolesRepo.deleteById(name);
		return Message.DELETE_SUCCESS;
	}

}
