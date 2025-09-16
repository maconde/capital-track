package com.maconde.model;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract sealed class Stakeholder permits CompanyStakeholder, PersonStakeholder {
    private Long id;
    private String name;
    public Stakeholder(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
