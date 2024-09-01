package com.example.gameStore.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum PlayerSupport {

    COOPERATIVE("COOPERATIVE"),

    LAN("LAN"),

    LOCAL_AND_PARTY("LOCAL_AND_PARTY"),

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

    public static List<String> getAllPlayerSupportString() {
        List<String> playerSupportList = new ArrayList<>();
        for (PlayerSupport ps : PlayerSupport.values()) {
            playerSupportList.add(ps.name);
        }
        return playerSupportList;
    }

    public static List<PlayerSupport> getAllPlayerSupport() {
        return Arrays.stream(PlayerSupport.values())
                .collect(Collectors.toList());
    }
}
