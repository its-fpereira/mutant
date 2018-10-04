package com.meli.mutant.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MutantService {

    public boolean isMutant(String[] dna) {
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
}
