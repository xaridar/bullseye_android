//Coded by Aakash Sell
package com.example.bullseye_android.database.survey;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bullseye_android.database.user.UserRoomDatabase;

import org.jsoup.nodes.Entities;

@Database(entities = {Survey.class}, version = 1, exportSchema = false)
public abstract class SurveyRoomDatabase extends RoomDatabase {
    public static SurveyRoomDatabase INSTANCE;
    public abstract SurveyDao surveyDao();

    public static SurveyRoomDatabase getInstance(final Context context){
        if(INSTANCE == null){
            synchronized (SurveyRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SurveyRoomDatabase.class, "survey_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
