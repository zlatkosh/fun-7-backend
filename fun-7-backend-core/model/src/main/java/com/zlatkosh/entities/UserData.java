package com.zlatkosh.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "user_data")
public class UserData {
    @Id
    @Column(name = "username", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    @Getter
    private String id;

    @NotNull
    @Column(name = "password", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String password;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    @Getter
    private CountryData country;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "username")
    @Getter
    @Setter
    private List<Role> roles = new java.util.ArrayList<>();

    @NotNull
    @Column(name = "time_zone", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    @Getter
    private String timeZone;
}