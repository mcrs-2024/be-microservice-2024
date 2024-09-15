package com.microservice.admin.entitys;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Users implements Serializable {/**
	 * 
	 */
	static final long serialVersionUID = -7866646107417363856L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	String name;
	
	@Column
	@Size(max = 20, message = "Username must be less than 20 characters")
	String username;
	
	@Column 
	@Size(max = 50, message = "Fullname must be less than 50 characters")
	String fullname;
	
	@Column 
	String hashPassword;
	
	@Column
	@Email
	String email;
	
	@Column 
	@CreationTimestamp // lấy thời gian hệ thống ngây tại thời điểm insert entitys vào database
	Timestamp createdDate;
	
	@Column
	String imgUrl;

	Boolean isDeleted;

	@ManyToMany
	Set<Roles> roles;
	
}
