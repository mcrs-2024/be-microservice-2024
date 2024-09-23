package com.microservice.admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.microservice.admin.entitys.Users;

@Repository
public interface UsersRepo extends JpaRepository<Users, String> {
	
	Users findByUsername(String username);

	List<Users> findByIsDeleted(Boolean isDeleted);

	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE users SET isDeleted = 1 WHERE username = ?", nativeQuery = true)
	void deleteLogical(String username);	

	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE users SET email = ?1, hashPassword = ?2, fullname =?3 WHERE username = ?4", nativeQuery = true)
	void update(String email, String hashPassword, String fullname, String username);

	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE users SET email = ?1, fullname = ?2 WHERE username = ?3 ", nativeQuery = true)
	void updateNonPass(String email, String fullname, String username);

	@Query(value = """
	SELECT * FROM users WHERE (:username IS NULL OR :username = username) AND
	                          (:fullName IS NULL OR :fullName = fullname) AND
	                          (:email IS NULL OR :email = email)
	""", nativeQuery = true)
	Page<Users> findAll(@Param("username") String username,
						@Param("fullName") String fullName,
						@Param("email") String email,
						Pageable pageable);
}
