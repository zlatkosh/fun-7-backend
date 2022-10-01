package com.zlatkosh.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDetails {
    private String username;
    private String countryCode;
    private String timeZone;
    private List<RoleDetails> roles;
}
