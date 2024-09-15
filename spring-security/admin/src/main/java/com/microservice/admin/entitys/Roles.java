package com.microservice.admin.entitys;

import java.io.Serializable;
import java.util.Set;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Roles implements Serializable {/**
	 * 
	 */
	static final long serialVersionUID = 5175438623266778535L;
	
	@Id
	@Column
	String name;
	
	@Column
	String description;

	@ManyToMany
	Set<Permission> permission;
}
