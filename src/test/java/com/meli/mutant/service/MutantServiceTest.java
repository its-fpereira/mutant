package com.meli.mutant.service;

import com.meli.mutant.MutantApplicationTests;
import com.meli.mutant.domain.Stats;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MutantServiceTest extends MutantApplicationTests {

    @Autowired
    MutantService mutantService;

    @Test
    public void should_return_true_for_valid_mutant_data_set() {
        // given:
        String[] dna = new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};

        // when:
        boolean isMutant = mutantService.isMutant(dna);

        // then:
        Assertions.assertThat(isMutant).as("isMutant").isTrue();
    }

    @Test
    public void should_return_false_for_invalid_mutant_data_set() {
        // given:
        String[] dna = new String[]{"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"};

        // when:
        boolean isMutant = mutantService.isMutant(dna);

        // then:
        Assertions.assertThat(isMutant).as("isMutant").isFalse();
    }

    @Test
    public void should_return_a_correct_ratio() {
        // given:
        String[] mutant1 = new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        String[] human1 = new String[]{"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"};
        String[] human2 = new String[]{"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCTTCA", "TCACTG"};
        mutantService.verifyMutantDna(mutant1).block();
        mutantService.verifyMutantDna(human1).block();
        mutantService.verifyMutantDna(human2).block();

        // when:
        Stats stats = mutantService.calculateStats().block();

        // then:
        Assertions.assertThat(stats).as("stats")
            .extracting("countMutantDna", "countHumanDna", "ratio")
            .contains(1, 2, .5);
    }
}
