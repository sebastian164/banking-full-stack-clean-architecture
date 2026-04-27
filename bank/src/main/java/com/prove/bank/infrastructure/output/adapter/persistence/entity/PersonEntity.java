package com.prove.bank.infrastructure.output.adapter.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class PersonEntity {
    @Column(nullable = false)
    private String name;
    private String gender;
    private Integer age;
    @Column(nullable = false, unique = true)
    private String identification;
    private String address;
    private String phone;
}
