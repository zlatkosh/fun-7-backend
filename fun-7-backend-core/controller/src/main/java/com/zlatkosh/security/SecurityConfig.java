package com.zlatkosh.security;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final List<String> PERMIT_ALL_PATHS = List.of("/login", "/access/refresh_token", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**");
    public static final List<String> USER_PATHS = List.of("/api/check-services/**");

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, Fun7AuthenticationFilter fun7AuthenticationFilter, Fun7AuthorizationFilter fun7AuthorizationFilter) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests((authz) -> authz
                        .antMatchers(PERMIT_ALL_PATHS.toArray(String[]::new)).permitAll()
                        .antMatchers().hasAuthority("ADMIN")
                        .antMatchers(USER_PATHS.toArray(String[]::new)).hasAuthority("USER")
                        .anyRequest().authenticated()
                )
                .addFilter(fun7AuthenticationFilter)
                .addFilter(new DefaultLoginPageGeneratingFilter())
                .addFilterBefore(fun7AuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.setUsersByUsernameQuery("SELECT username, password, true FROM user_data WHERE username = ?");
        users.setAuthoritiesByUsernameQuery("SELECT username, role_name FROM role WHERE username = ?");
        return users;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}