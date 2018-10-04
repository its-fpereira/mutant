package com.meli.mutant.controller;

import com.meli.mutant.domain.VerifyMutantDNARequest;
import com.meli.mutant.service.MutantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class MutantController {

    private final MutantService mutantService;

    public MutantController(MutantService mutantService) {
        this.mutantService = mutantService;
    }

    @PostMapping("/mutant/")
    public Mono<ResponseEntity<Void>> verifyMutantDna(@RequestBody VerifyMutantDNARequest request) {
        return Mono.defer(() -> Mono.just(mutantService.isMutant(request.getDna())))
            .map(isMutant -> {
                if (isMutant) return ResponseEntity.ok().build();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            });
    }
}
