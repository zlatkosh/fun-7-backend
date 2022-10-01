package com.zlatkosh.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CheckServicesRequest {
    @NotNull
    @NotEmpty
    private String timezone;
    private String userId;
    private String cc;
}
