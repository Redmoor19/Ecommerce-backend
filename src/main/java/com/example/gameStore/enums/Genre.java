package com.example.gameStore.enums;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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

    private final String name;

    Genre(String name) {
        this.name = name;
    }

    public static boolean isValidType(String type) {
        for (Genre fileType : Genre.values()) {
            if (fileType.name.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getAllGenres() {
        List<String> arrayList = new ArrayList<>();
        for (Genre se : Genre.values()) {
            arrayList.add(se.name);
        }
        return arrayList;
    }
}
