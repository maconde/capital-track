package com.maconde.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StakeholderParticipationDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 8041766417431658075L;

    private long id;
    private String stakeholderName;
    private double percentage;
}
