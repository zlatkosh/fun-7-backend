package com.zlatkosh.entities;

import lombok.Getter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Embeddable
public class RoleId implements Serializable {
    @Serial
    private static final long serialVersionUID = 4159451901615252600L;
    @NotNull
    @Column(name = "username", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String username;

    @NotNull
    @Column(name = "role_name", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    @Getter
    private String roleName;
}