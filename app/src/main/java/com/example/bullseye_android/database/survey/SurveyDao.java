//Coded by Aakash Sell
package com.example.bullseye_android.database.survey;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bullseye_android.database.user.User;

import java.util.List;

@Dao
public interface SurveyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Survey survey);

    @Query("SELECT * from survey_table")
    LiveData<List<Survey>> getAll();
}
