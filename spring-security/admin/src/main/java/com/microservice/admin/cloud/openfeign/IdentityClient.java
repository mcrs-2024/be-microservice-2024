package com.microservice.admin.cloud.openfeign;

import com.microservice.admin.dto.identity.TokenExchangeParam;
import com.microservice.admin.dto.identity.TokenExchangeResponse;
import com.microservice.admin.dto.identity.UserCreationParam;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "identity-client", url = "${identity-client.url}")
public interface IdentityClient {

    @PostMapping(value = "/realms/{application}/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenExchangeResponse exchangeToken(@PathVariable("application") String application, @QueryMap TokenExchangeParam param);

    @PostMapping(value = "/admin/realms/{application}/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createUser(
            @PathVariable("application") String application,
            @RequestHeader("authorization") String token,
            @RequestBody UserCreationParam param);

}
