package com.zlatkosh.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDetails {
    private String roleName;
    private String roleDescription;
}
