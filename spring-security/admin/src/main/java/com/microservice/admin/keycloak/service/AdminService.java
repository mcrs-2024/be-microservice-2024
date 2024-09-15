package com.microservice.admin.keycloak.service;

import com.microservice.admin.dto.request.RegistrationRequest;

public interface AdminService {
    Object register(RegistrationRequest request);

    Object findProfiles();
}
