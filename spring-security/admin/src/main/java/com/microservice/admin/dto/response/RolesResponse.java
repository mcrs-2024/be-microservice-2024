package com.microservice.admin.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolesResponse {
    String name;
    String description;
    Set<PermissionResponse> permission;
}
