package com.microservice.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.admin.entitys.Roles;

@Repository
public interface RolesRepo extends JpaRepository<Roles, String>{
	Roles findByDescription(String description);
}
