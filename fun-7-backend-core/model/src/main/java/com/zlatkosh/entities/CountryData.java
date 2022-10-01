package com.zlatkosh.entities;

import lombok.Getter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "country_data")
public class CountryData {
    @Id
    @Column(name = "country_id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "country_name", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String countryName;

    @NotNull
    @Column(name = "two_letter_code", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    @Getter
    private String twoLetterCode;
}