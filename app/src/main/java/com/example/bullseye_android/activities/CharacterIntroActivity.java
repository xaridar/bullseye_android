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
        chars.add(new CharDescriber("Bullseye the Bulldog", "Bullseye is the leader of the group, always in charge of group games!", "bulldog", this));
        chars.add(new CharDescriber("Carl the Caracal", "Carl is a playful caracal, ready to have some fun in Bullseye!", "caracal", this));
        chars.add(new CharDescriber("Katie the Cat", "Katie's a fancy cat, but she's got a couple tricks u her sleeve...", "cat", this));
        chars.add(new CharDescriber("Cheri the Chicken", "Cheri loves to cook - she makes the best omelette in town!", "chicken", this));
        chars.add(new CharDescriber("Kristoff the Cow", "Kristoff is always hungry for something. CHOMP!", "cow", this));
        chars.add(new CharDescriber("Filip the Fish", "Filip likes playing fish games and eating fish food.", "fish", this));
        chars.add(new CharDescriber("Farsch the Frog", "Farsch is a fast and fun frog friend.", "frog", this));
        chars.add(new CharDescriber("Guggerz the Goose", "It's amazing how much noise Guggerz can make for such a small bird.", "goose", this));
        chars.add(new CharDescriber("Harrison the Horse", "Harrison is the fastest one of the group - he can even beat Farsch in a race!", "horse", this));
        chars.add(new CharDescriber("Maurice the Monkey", "Maurice loves to swing around the jungle with all his friends!", "monkey", this));
        chars.add(new CharDescriber("Morty the Mouse", "He may be small, but Morty's a fierce competitor.", "mouse", this));
        chars.add(new CharDescriber("Oswald the Owl", "HOO! HOO!", "owl", this));
        chars.add(new CharDescriber("Pendleton the Panda", "This panda may look tough from the outside, but in his free time he collects quarters.", "panda", this));
        chars.add(new CharDescriber("Pearl the Pig", "Pearl's ready to play some games with everybody!", "pig", this));
        chars.add(new CharDescriber("Ravenna the Rabbit", "Ravenna hops around and paints pictures of carrots all day.", "rabbit", this));
        chars.add(new CharDescriber("Shaquini the Snake", "Shaquini loves slithering around in the field.", "snake", this));
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