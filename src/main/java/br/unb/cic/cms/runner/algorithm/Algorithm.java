package br.unb.cic.cms.runner.algorithm;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Algorithm {
    LNS_COMPACT_MODE("LNS_COMPACT_MODE"),
    LNS_FULL_MODE("LNS_FULL_MODE"),
    RANDOM_SEARCH("RANDOM_SEARCH"),
    NSGAII("NSGAII");

    private String name;

    Algorithm(String name) {
        this.name = name;
    }

    public static String validOptions() {
        return String.join(" | ", Arrays.asList(values())
                .stream()
                .map(v -> v.name)
                .collect(Collectors.toList()));
    }
}