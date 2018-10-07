package com.meli.mutant.repository;

import com.meli.mutant.domain.Human;
import com.meli.mutant.domain.Stats;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Divide;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Eq;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Cond;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@Repository
public class HumanRepositoryImpl implements HumanCustomRepository {

    private final ReactiveMongoTemplate template;

    public HumanRepositoryImpl(ReactiveMongoTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Stats> calculateStats() {
        Aggregation aggregation = newAggregation(
            group()
                .sum(Cond.when(Eq.valueOf("isMutant").equalToValue(true))
                    .then(1)
                    .otherwise(0)).as("countMutantDna")
                .sum(Cond.when(Eq.valueOf("isMutant").equalToValue(false))
                    .then(1)
                    .otherwise(0)).as("countHumanDna"),
            project("countMutantDna", "countHumanDna")
                .and(Cond
                    .when(Eq.valueOf("countHumanDna").equalToValue(0))
                    .thenValueOf("countMutantDna")
                    .otherwiseValueOf(Divide.valueOf("countMutantDna").divideBy("countHumanDna"))).as("ratio")
        );

        return template.aggregate(aggregation, Human.class, Stats.class).next();
    }
}
