package com.meli.mutant.service;

import com.meli.mutant.repository.HumanRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class MutantServiceTest {

    private MutantService mutantService;

    @Before
    public void setUp() {
        HumanRepository repository = Mockito.mock(HumanRepository.class);
        this.mutantService = new MutantService(repository);
    }

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
}
