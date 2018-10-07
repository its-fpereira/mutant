package com.meli.mutant.repository;

import com.meli.mutant.domain.Stats;
import reactor.core.publisher.Mono;

public interface HumanCustomRepository {

    Mono<Stats> calculateStats();

}
