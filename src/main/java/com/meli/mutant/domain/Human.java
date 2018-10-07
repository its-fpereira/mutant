package com.meli.mutant.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class Human {

    @Id
    private String id;
    @Indexed(unique = true)
    private int dna;
    private boolean isMutant;
}
