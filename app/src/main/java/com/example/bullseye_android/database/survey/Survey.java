//Coded by Aakash Sell
package com.example.bullseye_android.database.survey;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "survey_table")
public class Survey {


    @ColumnInfo(name="radioAnswer")
    private int radioAnswer;

    @ColumnInfo(name="inputText")
    private String inputText;

    @PrimaryKey(autoGenerate = true)
    private long id;

    public Survey(int radioAnswer, String inputText) {
        this.radioAnswer = radioAnswer;
        this.inputText = inputText;
    }

    public int getRadioAnswer() {
        return radioAnswer;
    }

    public void setRadioAnswer(int radioAnswer) {
        this.radioAnswer = radioAnswer;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
