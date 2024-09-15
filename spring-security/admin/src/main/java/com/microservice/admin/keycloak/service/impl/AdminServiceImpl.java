package com.microservice.admin.keycloak.service.impl;

import com.microservice.admin.cloud.openfeign.IdentityClient;
import com.microservice.admin.dto.identity.Credential;
import com.microservice.admin.dto.identity.TokenExchangeParam;
import com.microservice.admin.dto.identity.UserCreationParam;
import com.microservice.admin.dto.request.RegistrationRequest;
import com.microservice.admin.keycloak.service.AdminService;
import com.microservice.admin.mapper.modelmapper.UserMapperModel;
import com.microservice.admin.repository.UsersRepo;
import com.microservice.core.exception.ErrorNormalizer;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final IdentityClient identityClient;

    private final UserMapperModel userMapperModel;

    @Autowired
    private final UsersRepo usersRepo;

    private final ErrorNormalizer errorNormalizer;

    @Value("${identity-client.client-id}")
    @NonFinal
    String clientId;

    @Value("${identity-client.client-secret}")
    @NonFinal
    String clientSecret;

    @Value("${keycloak-name}")
    @NonFinal
    private String application;


    @Override
    @Transactional
    public Object register(RegistrationRequest request) {
        try {
            var token = identityClient.exchangeToken(application, TokenExchangeParam.builder()
                    .grant_type("client_credentials")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .build());

            log.info("tokenInfo {}", token);

            var creationResponse = identityClient.createUser(application,
                    "Bearer " + token.getAccessToken(),
                    UserCreationParam.builder()
                            .username(request.getUsername())
                            .email(request.getEmail())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .enabled(true)
                            .emailVerified(false)
                            .credentials(List.of(Credential.builder()
                                    .value("password")
                                    .temporary(false)
                                    .value(request.getPassword())
                                    .build()))
                            .build());

            String userId = extractUserId(creationResponse);
            log.info("userId {}", userId);
            var user = userMapperModel.toEntity(request, userId);
            return userMapperModel.toDto(usersRepo.save(user));
        } catch (FeignException ex) {
            throw errorNormalizer.handleKeyCloakException(ex);
        }
    }

    // test
    @Override
    public Object findProfiles() {
        return usersRepo.findAll();
    }

    private String extractUserId(ResponseEntity<?> response) {
        List<String> locationHeaders = response.getHeaders().get("Location");
        if (locationHeaders != null && !locationHeaders.isEmpty()) {
            String location = locationHeaders.get(0);
            String[] splitedStr = location.split("/");
            return splitedStr[splitedStr.length - 1];
        } else {
            return null;
        }
    }
}
