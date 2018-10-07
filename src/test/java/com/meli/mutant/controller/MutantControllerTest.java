package com.meli.mutant.controller;

import com.meli.mutant.MutantApplicationTests;
import com.meli.mutant.domain.VerifyMutantDNARequest;
import com.meli.mutant.service.MutantService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import reactor.core.publisher.Mono;

public class MutantControllerTest extends MutantApplicationTests {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    MutantService mutantService;

    @Test
    public void should_retrieve_200_when_is_mutant_request() {
        // given:
        VerifyMutantDNARequest request = new VerifyMutantDNARequest() {{
            setDna(new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"});
        }};

        // when:
        ResponseSpec exchange = webTestClient.post()
            .uri("/mutant/")
            .body(Mono.just(request), VerifyMutantDNARequest.class)
            .exchange();

        // then:
        exchange
            .expectStatus().is2xxSuccessful()
            .expectBody().isEmpty();
    }

    @Test
    public void should_retrieve_403_when_is_not_mutant_request() {
        // given:
        VerifyMutantDNARequest request = new VerifyMutantDNARequest() {{
            setDna(new String[]{"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"});
        }};

        // when:
        ResponseSpec exchange = webTestClient.post()
            .uri("/mutant/")
            .body(Mono.just(request), VerifyMutantDNARequest.class)
            .exchange();

        // then:
        exchange
            .expectStatus().isForbidden()
            .expectBody().isEmpty();
    }

    @Test
    public void should_retrieve_200_and_a_correct_ratio() {
        // given:
        String[] mutant1 = new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        String[] human1 = new String[]{"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"};
        String[] human2 = new String[]{"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCTTCA", "TCACTG"};
        mutantService.verifyMutantDna(mutant1).block();
        mutantService.verifyMutantDna(human1).block();
        mutantService.verifyMutantDna(human2).block();

        // when:
        ResponseSpec exchange = webTestClient.get()
            .uri("/stats/")
            .exchange();

        // then:
        exchange
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.countMutantDna").isEqualTo(1)
            .jsonPath("$.countHumanDna").isEqualTo(2)
            .jsonPath("$.ratio").isEqualTo(.5);
    }
}
