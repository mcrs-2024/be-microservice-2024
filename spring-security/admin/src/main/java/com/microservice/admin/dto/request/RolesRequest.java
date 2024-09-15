package com.microservice.admin.dto.request;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolesRequest {
    String name;
    String description;
    Set<PermissionRequest> permission;
}
