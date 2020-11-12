//Coded by Aakash Sell
package com.example.bullseye_android.database.survey;

import android.app.Application;
import android.provider.ContactsContract;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SurveyRepository {
    private SurveyDao dao;
    private LiveData<List<Survey>> surveys;
    private ExecutorService exService;

    public SurveyRepository(Application app){
        SurveyRoomDatabase database = SurveyRoomDatabase.getInstance(app);
        dao = database.surveyDao();
        surveys = dao.getAll();
        exService = Executors.newFixedThreadPool(100);
    }

    public void insert(Survey survey){
        exService.submit(new insertAsyncTask(dao, survey));
    }

    public LiveData<List<Survey>> getAllSurveys(){
        return surveys;
    }

    public static class insertAsyncTask implements Callable<Void> {
        SurveyDao dao;
        Survey survey;
        public insertAsyncTask(SurveyDao dao, Survey survey){
            this.survey = survey;
            this.dao = dao;
        }
        @Override
        public Void call() throws Exception {
            dao.insert(survey);
            return null;
        }
    }
}
