package com.example.gameStore.enums;

import java.util.ArrayList;
import java.util.List;

public enum PlayerSupport {

    COOPERATIVE("COOPERATIVE"),

    LAN("LAN"),

    LOCAL_AND_PARTY("LOCAL&PARTY"),

    MMO("MMO"),

    MULTIPLAYER("MULTIPLAYER"),

    ONLINE_COMPETITIVE("ONLINE_COMPETITIVE"),

    SINGLE_PLAYER("SINGLE_PLAYER");

    private final String name;

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

}
