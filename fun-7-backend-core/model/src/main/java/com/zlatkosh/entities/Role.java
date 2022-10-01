package com.zlatkosh.entities;

import lombok.Getter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Table(name = "role")
@Entity
public class Role {
    @EmbeddedId
    @Getter
    private RoleId id;

    @MapsId("username")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "username", nullable = false)
    private UserData username;

    @Column(name = "description")
    @Type(type = "org.hibernate.type.TextType")
    @Getter
    private String description;
}