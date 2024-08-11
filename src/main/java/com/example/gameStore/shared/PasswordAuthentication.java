package com.example.gameStore.shared;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordAuthentication {
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}

