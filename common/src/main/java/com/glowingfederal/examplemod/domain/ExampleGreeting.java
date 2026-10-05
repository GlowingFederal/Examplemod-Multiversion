package com.glowingfederal.examplemod.domain;

/** Portable domain logic; each platform owns delivery to the command sender. */
public final class ExampleGreeting {
    private ExampleGreeting() {}

    public static String create(String playerName) {
        if (playerName == null || playerName.trim().isEmpty()) {
            throw new IllegalArgumentException("playerName must not be blank");
        }
        return "Hello, " + playerName.trim() + "! Welcome to ExampleMod.";
    }
}
