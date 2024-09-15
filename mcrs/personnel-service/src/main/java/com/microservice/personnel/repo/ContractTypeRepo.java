package com.microservice.personnel.repo;

import com.microservice.personnel.entity.ContractType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractTypeRepo extends JpaRepository<ContractType, String> {
}
