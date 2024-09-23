package com.microservice.admin.service.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.microservice.admin.cloud.openfeign.ReportServiceClient;
import com.microservice.admin.dto.request.SearchUserRequest;
import com.microservice.admin.dto.response.UserResponse;
import com.microservice.admin.mapper.modelmapper.UserMapperModel;
import com.microservice.core.admin.constant.export.ExportUserDto;
import com.microservice.core.admin.constant.export.ExportUserRequest;
import com.microservice.admin.dto.request.UserRequest;
import com.microservice.admin.entitys.Roles;
import com.microservice.admin.entitys.Users;
import com.microservice.admin.mapper.mapstruct.UserMapper;
import com.microservice.admin.repository.InvalidatedTokenRepo;
import com.microservice.admin.repository.UsersRepo;
import com.microservice.admin.service.RolesService;
import com.microservice.admin.service.UsersService;
import com.microservice.core.constant.PageResponse;
import com.microservice.core.constant.RolesConstant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UsersService {
	
	private final BCryptPasswordEncoder bcrytPass = new BCryptPasswordEncoder();
	private final UsersRepo userRepo;
	private final RolesService rolesService;
	private final UserMapper userMapper;
	private final InvalidatedTokenRepo invalidatedTokenRepo;
	private final ReportServiceClient reportServiceClient;
	private final UserMapperModel userMapperModel;

	@Override
	public Users doLogin(Users usersRequest) {
		Users userReponse = userRepo.findByUsername(usersRequest.getUsername());
		if (ObjectUtils.isNotEmpty(userReponse)) {
			boolean checkPassword = bcrytPass.matches(usersRequest.getHashPassword(), userReponse.getHashPassword());
			return checkPassword ? userReponse : null;
		}
		return null;
	}

	@Override
	@Transactional(rollbackOn = {Exception.class, Throwable.class})
	public Users save(Users users) throws SQLException {
		Roles roles = rolesService.findByDescription(RolesConstant.USER);
		users.setIsDeleted(Boolean.FALSE);
		users.setHashPassword(bcrytPass.encode(users.getHashPassword()));
		return userRepo.saveAndFlush(users);
	}
	@Override
	public Object findAll(Pageable pageable, SearchUserRequest searchUserRequest) {
		Page<Users> usersPage = userRepo.findAll(searchUserRequest.getUsername(), searchUserRequest.getFullName(), searchUserRequest.getEmail(), pageable);
		if(!usersPage.isEmpty()){
			return null;
		}
		return new PageResponse<>(usersPage.map(userMapper::toDto));
	}

	@Override
	@Transactional(rollbackOn = {Exception.class, Throwable.class})
	public void deleteLogical(String username) {
		userRepo.deleteLogical(username);
	}

	@Override
	public Users findByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	@Override
	@Transactional(rollbackOn = {Exception.class, Throwable.class})
	public void update(Users user) {
		if(ObjectUtils.isNotEmpty(user) && StringUtils.isEmpty(user.getHashPassword())) {
			userRepo.updateNonPass(user.getEmail(), user.getFullname(), user.getUsername());
		} else {
			String hashPassword = bcrytPass.encode(user.getHashPassword());
			user.setHashPassword(hashPassword);
			userRepo.update(user.getEmail(), hashPassword, user.getFullname(), user.getUsername());
		}
	}

	@Override
	public Object updateUsers(UserRequest request) {
		if(request != null){
			Users users = userMapper.toUser(request);
			if(ObjectUtils.isNotEmpty(users)){
				users = userRepo.save(users);
			}
			return userMapper.toDto(users);
		}
		return null;
	}

	@Override
	public ResponseEntity<ByteArrayResource> export(String typeFile, String fileName) {
		List<Users> entity = userRepo.findAll();
		List<ExportUserDto> list = userMapperModel.dtoList(entity);
		return reportServiceClient.report(ExportUserRequest.builder()
				.fileName(fileName)
				.typeFile(typeFile)
				.userResponses(list)
				.build());
	}
}
