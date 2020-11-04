// Designed and Coded by Aakash
package com.example.bullseye_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.bullseye_android.R;
import com.example.bullseye_android.games.memory.MemoryActivity;

public class Survey extends AppCompatActivity {
    EditText surveyText;
    RadioGroup radioGroup;
    Button submit;
    Button goBack;
    String surveyTextValue;
    int radioAnswer = -1;
    RadioButton selectedRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        run();
    }

    public void run(){
        surveyText = findViewById(R.id.surveyTextResponse);
        radioGroup = findViewById(R.id.surveyRadioGroup);
        submit = findViewById(R.id.submit);
        goBack = findViewById(R.id.goBack);

        goBack.setOnClickListener(v -> startActivity(new Intent(this, UserDashboardActivity.class)));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                    selectedRadioButton = findViewById(selectedRadioButtonId);
                    if(selectedRadioButtonId == 2131296365){
                        Log.i("button 1", "returns 0");
                        radioAnswer = 0;
                    }
                    else if(selectedRadioButtonId == 2131296366){
                        Log.i("button 2", "returns 1");
                        radioAnswer = 0;
                    }
                    else if(selectedRadioButtonId == 2131296367){
                        Log.i("button 3", "returns 2");
                        radioAnswer = 0;
                    }
                }
               surveyTextValue = surveyText.getText().toString();

                if(selectedRadioButtonId != -1 && !TextUtils.isEmpty(surveyText.getText().toString().trim())) {
                    startActivity(new Intent(Survey.this, UserDashboardActivity.class));
                }
                else{
                    Toast.makeText(Survey.this, "Please complete the survey before submitting.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    public String getRadioAnswers(int i){
        String[] radioAnswers = {"good", "neutral", "bad"};
        if(i >= 0 && i < radioAnswers.length){
            return radioAnswers[i];
        }
        else{
            return "index out of bounds";
        }
    }

}