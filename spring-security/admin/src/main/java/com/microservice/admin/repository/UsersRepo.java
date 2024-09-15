package com.microservice.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.microservice.admin.entitys.Users;

@Repository
public interface UsersRepo extends JpaRepository<Users, String> {
	
	Users findByUsername(String username);
	// lấy ra các user còn tồn tại chưa bị xóa
	List<Users> findByIsDeleted(Boolean isDeleted);
	// delete user
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE users SET isDeleted = 1 WHERE username = ?", nativeQuery = true)
	void deleteLogical(String username);	
	// update tài khoản
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE users SET email = ?1, hashPassword = ?2, fullname =?3 WHERE username = ?4", nativeQuery = true)
	void update(String email, String hashPassword, String fullname, String username);
	// update ko có password
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE users SET email = ?1, fullname = ?2 WHERE username = ?3 ", nativeQuery = true)
	void updateNonPass(String email, String fullname, String username);

}
