package com.zlatkosh.security;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Builder
@Data
public final class JwtMetadata {
    private final String username;
    private List<String> roles;
    private Date refreshTokenExpiryDate;
}