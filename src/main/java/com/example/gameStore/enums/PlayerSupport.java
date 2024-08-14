package com.example.gameStore.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum PlayerSupport {

    COOPERATIVE("COOPERATIVE"),

    LAN("LAN"),

    LOCAL_AND_PARTY("LOCAL&PARTY"),

    MMO("MMO"),

    MULTIPLAYER("MULTIPLAYER"),

    ONLINE_COMPETITIVE("ONLINE_COMPETITIVE"),

    SINGLE_PLAYER("SINGLE_PLAYER");

    private final String name;
    private static final Map<String, PlayerSupport> NAME_TO_ENUM = new HashMap<>();

    static {
        for (PlayerSupport ps : PlayerSupport.values()) {
            NAME_TO_ENUM.put(ps.getName(), ps);
        }
    }

    PlayerSupport(String name) {
        this.name = name;
    }

    public static List<String> getAllPlayerSupport() {
        List<String> playerSupportList = new ArrayList<>();
        for (PlayerSupport ps : PlayerSupport.values()) {
            playerSupportList.add(ps.name);
        }
        return playerSupportList;
    }

    public static PlayerSupport fromString(String name) {
        return NAME_TO_ENUM.get(name.toUpperCase());
    }


}
