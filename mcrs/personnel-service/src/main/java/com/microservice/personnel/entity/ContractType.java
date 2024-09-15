package com.microservice.personnel.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contract_type")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "CODE_TYPE")
    String codeType;

    @Column(name = "TYPE_NAME")
    String typeName;

}
