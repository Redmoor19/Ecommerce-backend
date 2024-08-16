package com.example.gameStore.enums;


import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum Genre {

    ACTION("ACTION"),
    ADVENTURE("ADVENTURE"),
    FIGHTING("FIGHTING"),
    PLATFORM("PLATFORM"),
    PUZZLE("PUZZLE"),
    RACING("RACING"),
    RPG("RPG"),
    SHOOTER("SHOOTER"),
    SIMULATION("SIMULATION"),
    SPORTS("SPORTS"),
    STRATEGY("STRATEGY");

    private static final Map<String, Genre> NAME_TO_ENUM = new HashMap<>();
    private final String name;

    Genre(String name) {
        this.name = name;
    }

    static {
        for (Genre ps : Genre.values()) {
            NAME_TO_ENUM.put(ps.getName(), ps);
        }
    }

    public static boolean isValidType(String type) {
        for (Genre fileType : Genre.values()) {
            if (fileType.name.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getAllGenresString() {
        List<String> arrayList = new ArrayList<>();
        for (Genre se : Genre.values()) {
            arrayList.add(se.name);
        }
        return arrayList;
    }

    public static List<Genre> getAllGenres() {
        return Arrays.stream(Genre.values())
                .collect(Collectors.toList());
    }

    public static Genre fromString(String name) {
        return NAME_TO_ENUM.get(name.toUpperCase());
    }

}
