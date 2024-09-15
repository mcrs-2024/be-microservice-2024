package com.microservice.personnel.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractTypeRequest {
    String codeType;
    @NotNull(message = "không được bỏ trống")
    String typeName;
}
