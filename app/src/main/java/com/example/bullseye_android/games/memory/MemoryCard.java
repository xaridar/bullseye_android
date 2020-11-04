package com.example.bullseye_android.games.memory;

import com.example.bullseye_android.R;

public class MemoryCard {
    private String type;
    private int backColor;
    public static int FRONT_COLOR = R.color.memCardFront;
    public static String[] CARD_TYPES = {"Bulldog", "Caracal", "Cat", "Chicken", "Cow", "Mouse", "Fish", "Frog", "Goose", "Horse", "Monkey", "Owl", "Panda", "Pig", "Rabbit", "Snake"};
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

    public int getBackColor() { return backColor; }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }

}
