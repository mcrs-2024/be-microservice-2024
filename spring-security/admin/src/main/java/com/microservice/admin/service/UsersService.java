package com.microservice.admin.service;

import java.sql.SQLException;
import java.util.List;

import com.microservice.admin.dto.request.SearchUserRequest;
import com.microservice.admin.dto.request.UserRequest;
import com.microservice.admin.entitys.Users;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface UsersService {
	
	Object findAll(Pageable pageable, SearchUserRequest searchUserRequest);
	Users doLogin(Users usersRequest);
	Users save(Users users) throws SQLException;
	Users findByUsername(String username);
	void deleteLogical(String username);
	void update(Users user);
	Object updateUsers(UserRequest request);

    ResponseEntity<ByteArrayResource> export(String typeFile, String fileName);
}
