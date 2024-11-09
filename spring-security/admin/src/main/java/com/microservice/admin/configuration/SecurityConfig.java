package com.microservice.admin.configuration;

import com.microservice.admin.enums.Role;
import com.microservice.admin.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * cấu hình security trên method
 * @EnableMethodSecurity
 * @PreAuthorize("hasAnyRole('ADMIN')") mặc định là ROLE_
 * @PostAuthorize("returnObject.username == authentication.name")
 * PreAuthorize trước khi thực hiện method
 * PostAuthorize sau khi thực hiện method kiểm tra quyền
 * @PreAuthorize("hasAuthority('ADMIN')") map chính xác
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomJwtDecoder customJwtDecoder;
    private final String[] PUBLIC_ENDPOINT_GET = {};
    private final String[] PUBLIC_ENDPOINT_POST = {"/admin/create",
            "/admin/login", "/admin/introspect-request",
            "/admin/logout", "/admin/refresh-token","/keycloak/register"};
    private final String[] PRIVATE_ENDPOINT_ADMIN_GET = {};

    /**
     * .hasAnyAuthority() cấu hình trực tiếp
     * .hasRole() cấu hình linh động
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINT_GET).permitAll()
                                .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT_POST).permitAll()
                                .requestMatchers(HttpMethod.GET,PRIVATE_ENDPOINT_ADMIN_GET)
                                .hasRole(Role.ADMIN.name())
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
        return http.build();
    }

    /**
     * cấu hình lại ROLE
     * @return
     */
    @Bean
    JwtAuthenticationConverter  jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain keycloakFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/**")  // Các API sử dụng Keycloak
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/public/**").permitAll()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
