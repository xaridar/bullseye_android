// Elliot coded music package
package com.example.bullseye_android.music;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bullseye_android.R;

public interface MusicActivity{
    default int getMusicId() {
        return R.raw.bg;
    }
}
