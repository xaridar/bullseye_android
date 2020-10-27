// Elliot coded music package
package com.example.bullseye_android.music;

import com.example.bullseye_android.R;

public interface MusicActivity{
    default int getMusicId() {
        return R.raw.bg;
    }

    default boolean startImmediately() {
        return true;
    }

    default void startMusic() {
        MusicManager.getInstance().start();
    }
}
