//Elliot coded and designed
package com.example.bullseye_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.example.bullseye_android.R;
import com.example.bullseye_android.music.MusicActivity;

import java.util.HashMap;

public class AvatarChooserActivity extends MusicActivity {

    String currentAva;

    HashMap<String, ImageButton> avatars = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_chooser);
        if (getIntent() == null) {
            finish();
            return;
        }

        ImageButton defaultAva = findViewById(R.id.defaultAva);
        ImageButton boyAva = findViewById(R.id.boyAva);
        ImageButton girlAva= findViewById(R.id.girlAva);
        ImageButton archerAva = findViewById(R.id.archerAva);
        ImageButton logoAva = findViewById(R.id.logoAva);
        ImageButton logo_altAva = findViewById(R.id.logo_altAva);

        avatars.put("default", defaultAva);
        avatars.put("boy", boyAva);
        avatars.put("girl", girlAva);
        avatars.put("archer", archerAva);
        avatars.put("logo", logoAva);
        avatars.put("logo_alt", logo_altAva);

        currentAva = getIntent().getStringExtra("avatar");
        changeAvatar();

        defaultAva.setOnClickListener(avatarListener);
        boyAva.setOnClickListener(avatarListener);
        girlAva.setOnClickListener(avatarListener);
        archerAva.setOnClickListener(avatarListener);
        logoAva.setOnClickListener(avatarListener);
        logo_altAva.setOnClickListener(avatarListener);

        findViewById(R.id.backToUserSignUp).setOnClickListener(view -> {
            finish();
        });
    }

    private View.OnClickListener avatarListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            currentAva = view.getContentDescription().toString();
            changeAvatar();
        }
    };

    @Override
    public void finish() {
        Intent backIntent = new Intent();
        backIntent.putExtra("avatar", currentAva);
        setResult(RESULT_OK, backIntent);
        super.finish();
    }

    private void changeAvatar() {
        avatars.get(currentAva).setBackground(getDrawable(R.drawable.ic_circle));
        for (ImageButton ava : avatars.values()) {
            if (ava != avatars.get(currentAva)) {
                ava.setBackgroundColor(getColor(android.R.color.transparent));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}