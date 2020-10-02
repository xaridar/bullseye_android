package com.example.bullseye_android.games.memory;

public class MemoryCard {
    private String type;
    public static String[] CARD_TYPES = {"Cow", "Sheep", "Horse", "Pig", "Dog", "Cat", "Human", "Chicken", "Llama", "Goat", "Duck", "Donkey", "Rabbit", "Deer", "Fish"};
    private boolean faceDown = true;
    public MemoryCard(String type) {
        this.type = type;
    }

    public static String[] getTypes() {
        return CARD_TYPES;
    }

    public String getType() {
        return type;
    }

    public void setFaceDown(boolean faceDown) {
        this.faceDown = faceDown;
    }

    public boolean isFaceDown() {
        return faceDown;
    }
}
