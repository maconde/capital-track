package com.maconde.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ownership {
    private Stakeholder owner;
    private Stakeholder target;
    private double percentage;
}
