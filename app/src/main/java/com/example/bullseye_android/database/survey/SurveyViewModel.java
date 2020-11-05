package com.example.bullseye_android.database.survey;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SurveyViewModel extends AndroidViewModel {

    private SurveyRepository surveyRepository;
    private LiveData<List<Survey>> surveys;

    public SurveyViewModel(@NonNull Application application){
        super(application);

        surveyRepository = new SurveyRepository(application);
        surveys = surveyRepository.getAllSurveys();
    }

    public void insert(Survey survey){
        surveyRepository.insert(survey);
    }
    public LiveData<List<Survey>> getAll(){
        return surveys;
    }


}
