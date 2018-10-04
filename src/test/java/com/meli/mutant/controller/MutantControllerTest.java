package com.meli.mutant.controller;

import com.meli.mutant.MutantApplicationTests;
import com.meli.mutant.domain.VerifyMutantDNARequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import reactor.core.publisher.Mono;

public class MutantControllerTest extends MutantApplicationTests {

    @Autowired
    WebTestClient webTestClient;

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
}
