package com.microservice.admin.configuration;

import com.microservice.core.admin.constant.dto.request.IntrospectRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {
    @NonFinal
    @Value("${jwt.signerKey}")
    private String key;

    private final ConfigJwt configJwt;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    /**
     * Kiểm tra token còn hiệu lực không
     * @param token
     * @return
     * @throws JwtException
     */
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            var response = configJwt.introspectRespponse(IntrospectRequest
                    .builder()
                    .token(token)
                    .build());
            if (!response.isValid())
                throw new JwtException("Cút");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        /**
         * nếu token còn hiệu lực
         */
        if(Objects.isNull(nimbusJwtDecoder)){
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(),
                    "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        System.out.println(nimbusJwtDecoder);
        return nimbusJwtDecoder.decode(token);
    }
}
