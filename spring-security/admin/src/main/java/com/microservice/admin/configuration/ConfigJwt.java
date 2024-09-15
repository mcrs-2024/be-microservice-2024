package com.microservice.admin.configuration;

import com.microservice.admin.dto.response.RefreshTokenNew;
import com.microservice.admin.entitys.InvalidatedToken;
import com.microservice.core.admin.constant.dto.request.IntrospectRequest;
import com.microservice.core.admin.constant.dto.response.IntrospectResponse;
import com.microservice.core.request.LogoutRequest;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.microservice.admin.dto.request.RefreshRequest;
import com.microservice.admin.entitys.Users;
import com.microservice.admin.repository.InvalidatedTokenRepo;
import com.microservice.admin.repository.RolesRepo;
import com.microservice.admin.repository.UsersRepo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Log4j2
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class ConfigJwt {

    @NonFinal
    @Value("${jwt.signerKey}")
    private String key;

    @NonFinal
    @Value("${jwt.valid-duration}")
    private long valid_duration;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    private long refreshable_duration;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private RolesRepo rolesRepo;

    @Autowired
    private InvalidatedTokenRepo invalidatedTokenRepo;

    /**
     * tạo token có phân quyền
     * @param user
     * @return
     */
    public String generateToken(Users user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512 );
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("dang@gmail.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(valid_duration,
                                ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header,payload );
        try {
            jwsObject.sign(new MACSigner(key.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * lấy ra role của người dùng để tạo quyền truy cập cho token
     * @param users
     * @return
     */
    private String buildScope(Users users){
        StringJoiner stringJoiner = new StringJoiner(" ");

        if(!CollectionUtils.isEmpty(users.getRoles())){
            users.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if(!CollectionUtils.isEmpty(role.getPermission()))
                    role.getPermission().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                    });
            });
        }

        return stringJoiner.toString();
    }

    /**
     * kiểm tra token có hợp lệ không
     * @param request
     * @return
     * @throws JOSEException
     * @throws ParseException
     */
    public IntrospectResponse introspectRespponse(IntrospectRequest request) throws Exception {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (Exception e){
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws Exception {
        JWSVerifier verifier = new MACVerifier(key.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date exPiTyTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                .toInstant().plus(refreshable_duration, ChronoUnit.SECONDS)
                .toEpochMilli())
        : signedJWT.getJWTClaimsSet().getExpirationTime();
        var verify = signedJWT.verify(verifier);
        if(!(verify && exPiTyTime.after(new Date())))
            throw new Exception("Cút!");
        if(invalidatedTokenRepo.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new Exception("Cút");
        return signedJWT;
    }

    /**
     * Lấy ra username đăng nhập
     */
    public Users userRequest(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        return usersRepo.findByUsername(name);
    }

    /**
     * logout token
     *
     * @return
     */
    public Object logout(LogoutRequest request) {
        try {
            var signToken = verifyToken(request.getToken(), true);
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
            InvalidatedToken invalidatedToken = InvalidatedToken
                    .builder()
                    .idToken(jit)
                    .expiryTime(expiryTime)
                    .build();
            invalidatedTokenRepo.save(invalidatedToken);
            return null;
        } catch (Exception e){
            log.info("Hết hạn");
            return e;
        }
    }

    /**
     * refreshToken
     */
    public RefreshTokenNew refreshToken(RefreshRequest request) throws Exception {
        var signed_jwt = verifyToken(request.getToken(),true);
        var jit = signed_jwt.getJWTClaimsSet().getJWTID();
        var expiryTime = signed_jwt.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken
                .builder()
                .idToken(jit)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepo.save(invalidatedToken);
        var username = signed_jwt.getJWTClaimsSet().getSubject();
        var user = usersRepo.findByUsername(username);
        var tokeNew = generateToken(user);
        return RefreshTokenNew
                .builder()
                .token(tokeNew)
                .build();
    }
}
