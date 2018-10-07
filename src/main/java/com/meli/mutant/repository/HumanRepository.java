package com.meli.mutant.repository;

import com.meli.mutant.domain.Human;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface HumanRepository extends ReactiveMongoRepository<Human, String> {

    Mono<Human> findHumanByDna(int dna);

}
