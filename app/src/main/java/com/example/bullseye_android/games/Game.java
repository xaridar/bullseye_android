package com.example.bullseye_android.games;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.example.bullseye_android.R;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public interface Game {
    void unpause();

    FragmentManager getSupportFragmentManager();

    String getGame();

    void pause(View view);

    default void confetti(KonfettiView konfettiView, Context context){
        final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_bullseye_logo);
        if (drawable != null) {
            final Shape.DrawableShape drawableShape = new Shape.DrawableShape(drawable, true);
            konfettiView.build()
                    .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 5f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(drawableShape)
                    .addSizes(new Size(12, 5f))
                    .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                    .streamFor(300, 5000L);
        }
    }
}
