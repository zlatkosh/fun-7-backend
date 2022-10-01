package com.zlatkosh.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class CheckServicesResponse {
    public static final String ENABLED_DISABLED = "enabled|disabled";

    @NotNull @NotEmpty @Pattern( regexp = ENABLED_DISABLED)
    private String multiplayer;
    @NotNull @NotEmpty @Pattern( regexp = ENABLED_DISABLED)
    @JsonAlias("user-support")
    private String userSupport;
    @NotNull @NotEmpty @Pattern( regexp = ENABLED_DISABLED)
    private String ads;
}
