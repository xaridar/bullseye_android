package com.example.bullseye_android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.bullseye_android.R;
import com.example.bullseye_android.util.CharAdapter;
import com.example.bullseye_android.util.CharDescriber;

import java.util.ArrayList;
import java.util.List;

public class CharacterIntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_intro);
        RecyclerView rv = findViewById(R.id.charRV);
        List<CharDescriber> chars = createCharDescribers();
        rv.setAdapter(new CharAdapter(this, chars));
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<CharDescriber> createCharDescribers() {
        List<CharDescriber> chars = new ArrayList<>();
        chars.add(new CharDescriber("Bullseye the Bulldog", "Bullseye's mascot!", "bulldog", this));
        chars.add(new CharDescriber("Carl the Caracal", "Carl is a playful caracal, ready to have some fun in Bullseye!", "caracal", this));
        chars.add(new CharDescriber("Katie the Cat", "Bullseye's mascot!", "cat", this));
        chars.add(new CharDescriber("Cheri the Chicken", "Bullseye's mascot!", "chicken", this));
        chars.add(new CharDescriber("Kristoff the Cow", "Bullseye's mascot!", "cow", this));
        chars.add(new CharDescriber("Filip the Fish", "Bullseye's mascot!", "fish", this));
        chars.add(new CharDescriber("Farsch the Frog", "Bullseye's mascot!", "frog", this));
        chars.add(new CharDescriber("Guggerz the Goose", "Bullseye's mascot!", "goose", this));
        chars.add(new CharDescriber("Horace the Horse", "Bullseye's mascot!", "horse", this));
        chars.add(new CharDescriber("Maurice the Monkey", "Bullseye's mascot!", "monkey", this));
        chars.add(new CharDescriber("Morty the Mouse", "Bullseye's mascot!", "mouse", this));
        chars.add(new CharDescriber("Oswald the Owl", "Bullseye's mascot!", "owl", this));
        chars.add(new CharDescriber("Pendleton the Panda", "Bullseye's mascot!", "panda", this));
        chars.add(new CharDescriber("Pearl the Pig", "Bullseye's mascot!", "pig", this));
        chars.add(new CharDescriber("Ravenna the Rabbit", "Bullseye's mascot!", "rabbit", this));
        chars.add(new CharDescriber("Shaquini the Snake", "Bullseye's mascot!", "snake", this));
        return chars;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void back(View view) {
        finish();
    }
}