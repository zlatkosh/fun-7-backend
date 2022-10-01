package com.zlatkosh.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zlatkosh.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class JwtUtility {
    private static final String SECRET_KEY = "fun-7-secret";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
    private static final String CLAIM_ROLES = "roles";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final String ISSUER = "fun-7";

    private final ApplicationContext context;
    @Value("${app.security.expiry.access-token}")
    private long accessTokenExpiry;

    public UsernamePasswordAuthenticationToken verifyAccessTokenString(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        String username = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaim(CLAIM_ROLES).asList(String.class);
        List<GrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    public DecodedJWT getDecodedJWT(String token) {
        JWTVerifier verifier = JWT.require(ALGORITHM).build();
        return verifier.verify(token);
    }

    public JwtMetadata extractMetadataFromTokenString(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        String username = decodedJWT.getSubject();
        UserService userService = context.getBean(UserService.class);
        if (userService.userExists(username)) {
            return JwtMetadata.builder()
                    .username(username)
                    .roles(decodedJWT.getClaim(CLAIM_ROLES).asList(String.class))
                    .build();
        }
        return null;
    }

    public String generateAccessToken(JwtMetadata jwtMetadata) {
        return JWT.create()
                .withSubject(jwtMetadata.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpiry))
                .withIssuer(ISSUER)
                .withClaim(CLAIM_ROLES, jwtMetadata.getRoles())
                .sign(ALGORITHM);
    }

    public String generateRefreshToken(JwtMetadata jwtMetadata) {
        return JWT.create()
                .withSubject(jwtMetadata.getUsername())
                .withIssuedAt(new Date())
                .withIssuer(ISSUER)
                .withExpiresAt(jwtMetadata.getRefreshTokenExpiryDate())
                .sign(ALGORITHM);
    }
}
