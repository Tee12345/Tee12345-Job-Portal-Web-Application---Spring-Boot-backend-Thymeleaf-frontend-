package com.babatunde.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String logo;

    public Company() {}

    public Company(Integer id, String name, String logo) {
        this.id = id;
        this.name = name;
        this.logo = logo;
    }
}
