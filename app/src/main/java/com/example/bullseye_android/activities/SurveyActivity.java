// Designed and Coded by Aakash
package com.example.bullseye_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.bullseye_android.R;
import com.example.bullseye_android.database.survey.Survey;
import com.example.bullseye_android.database.survey.SurveyViewModel;

import java.util.List;

public class SurveyActivity extends AppCompatActivity {
    EditText surveyText;
    RadioGroup radioGroup;
    Button submit;
    Button goBack;
    String surveyTextValue;
    int radioAnswer = -1;
    RadioButton selectedRadioButton;
    SurveyViewModel viewModel;

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
        viewModel = ViewModelProviders.of(this).get(SurveyViewModel.class);

        goBack.setOnClickListener(v -> startActivity(new Intent(this, UserDashboardActivity.class)));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                    if(selectedRadioButtonId == R.id.button1){
                        Log.i("button 1", "returns 0");
                        radioAnswer = 0;
                    }
                    else if(selectedRadioButtonId == R.id.button2){
                        Log.i("button 2", "returns 1");
                        radioAnswer = 1;
                    }
                    else if(selectedRadioButtonId == R.id.button3){
                        Log.i("button 3", "returns 2");
                        radioAnswer = 2;
                    }
                }
               surveyTextValue = surveyText.getText().toString();

                if(selectedRadioButtonId != -1 && !TextUtils.isEmpty(surveyText.getText().toString().trim())) {
                    viewModel.insert(new Survey(radioAnswer, surveyText.getText().toString()));
                    LiveData<List<Survey>> data = viewModel.getAll();
                    data.observe(SurveyActivity.this, surveys -> Log.i("Database change", surveys.toString()));
                    finish();
                }
                else{
                    Toast.makeText(SurveyActivity.this, "Please complete the survey before submitting.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    public static String getRadioAnswers(int i){
        String[] radioAnswers = {"good", "neutral", "bad"};
        if(i >= 0 && i < radioAnswers.length){
            return radioAnswers[i];
        }
        else{
            return "index out of bounds";
        }
    }

}