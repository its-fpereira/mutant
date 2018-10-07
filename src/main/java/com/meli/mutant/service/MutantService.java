package com.meli.mutant.service;

import com.meli.mutant.domain.Human;
import com.meli.mutant.domain.Stats;
import com.meli.mutant.repository.HumanRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MutantService {

    private final HumanRepository humanRepository;

    public MutantService(HumanRepository humanRepository) {
        this.humanRepository = humanRepository;
    }

    public Mono<Boolean> verifyMutantDna(String[] dna) {
        return humanRepository.findHumanByDna(getDnaHash(dna))
            .switchIfEmpty(Mono.defer(() -> verifyAndSaveDna(dna)))
            .map(Human::isMutant);
    }

    public Mono<Stats> calculateStats() {
        return humanRepository.calculateStats();
    }

    boolean isMutant(String[] dna) {
        long sum = 0;
        int n = dna.length;

        for (int x = 0; x < n; x++) {
            if (sum > 1) return true;

            sum += getHorizontalCount(dna[x]);

            for (int y = 0; y < n; y++) {
                if (sum > 1) return true;
                if (x + 3 >= n) break;
                sum += getVerticalCount(dna, x, y);

                if (y + 3 >= n) break;
                sum += getDiagonalCount(dna, x, y);
            }
        }

        return sum > 1;
    }

    private Mono<Human> verifyAndSaveDna(String[] dna) {
        Human human = new Human();
        human.setDna(getDnaHash(dna));
        human.setMutant(isMutant(dna));
        return humanRepository.save(human);
    }

    private long getVerticalCount(String[] dna, int x, int y) {
        String sequence = "" + dna[x].charAt(y) + dna[x + 1].charAt(y) + dna[x + 2].charAt(y) + dna[x + 3].charAt(y);
        return verifyMutantSequence(sequence);
    }

    private long getHorizontalCount(String dna) {
        return verifyMutantSequence(dna);
    }

    private long getDiagonalCount(String[] dna, int x, int y) {
        String sequence = "" + dna[x].charAt(y) + dna[x + 1].charAt(y + 1) + dna[x + 2].charAt(y + 2) + dna[x + 3].charAt(y + 3);
        return verifyMutantSequence(sequence);
    }

    private long verifyMutantSequence(String sequence) {
        String expression = "(AAAA|TTTT|CCCC|GGGG)";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(sequence);
        return matcher.results().count();
    }

    private int getDnaHash(String[] dna) {
        return String.join("", dna).hashCode();
    }
}
