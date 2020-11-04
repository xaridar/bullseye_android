package com.example.bullseye_android.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class CharDescriber {

    String name;
    String description;
    int resource;

    public CharDescriber(String name, String desc, String animal, Context context) {
        this.name = name;
        description = desc;
        resource = context.getResources().getIdentifier("ic_mem_img_" + animal, "drawable", "com.example.bullseye_android");
    }
}
