package com.example.bullseye_android.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity(tableName = "user_table")
public class User {

    public static final int ALL_GAMES = 0;
    public static final int GAME_MEMORY_EASY = 1;
    public static final int GAME_MEMORY_NORMAL = 2;
    public static final int GAME_MEMORY_HARD = 3;
    public static final int GAME_SORTING_SLOW = 4;
    public static final int GAME_SORING_FAST = 5;

    public static final int POINTS = 100;
    public static final int ACC = 101;
    public static final int TIME = 102;
    public static final int statsTypes = 3;

    @ColumnInfo(name = "admin")
    private boolean admin;

    @PrimaryKey
    private long id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "playTime")
    private long[] playTime;

    @ColumnInfo(name = "gamesPlayed")
    private int[] gamesPlayed;

    @ColumnInfo(name = "accuracy")
    private float[] accuracy;

    @Nullable
    @ColumnInfo(name="email")
    private String email;

    @Nullable
    @ColumnInfo(name="password")
    private String password;

    @ColumnInfo(name = "focusPoints")
    private int[] focusPoints;

    @ColumnInfo(name = "lastGames")
    private List<Number[]>[] lastGames;


    public User(@NonNull String name, long id) {
        this.name = name;
        this.admin = false;
        this.id = id;
        playTime = new long[]{0, 0, 0, 0, 0, 0};
        email = null;
        password = null;
        accuracy = new float[]{0, 0, 0, 0, 0};
        focusPoints = new int[] {0, 0, 0, 0, 0, 0};
        gamesPlayed = new int[] {0, 0, 0, 0, 0};
        lastGames = new ArrayList[]{new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>()};
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public long[] getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long[] playTime) {
        this.playTime = playTime;
    }

    public float[] getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float[] accuracy) {
        this.accuracy = accuracy;
    }

    public int[] getFocusPoints() {
        return focusPoints;
    }

    public void setFocusPoints(int[] focusPoints) {
        this.focusPoints = focusPoints;
    }

    public int[] getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int[] gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public List<Number[]>[] getLastGames() {
        return lastGames;
    }

    public void setLastGames(List<Number[]>[] lastGames) {
        this.lastGames = lastGames;
    }

    private void addPlayTime(int game, long timeToAdd) {
        playTime[ALL_GAMES] += timeToAdd;
        playTime[game] += timeToAdd;
    }

    private void addAcc(int game, float acc) {
        float oldAcc = accuracy[game];
        oldAcc *= gamesPlayed[game];
        oldAcc += acc;
        oldAcc /= gamesPlayed[game] + 1;
        accuracy[game] = oldAcc;
    }

    private void addPoints(int game, int points) {
        focusPoints[ALL_GAMES] += points;
        focusPoints[game] += points;
    }

    public void addGame(int game, float acc, long time, int pointsToAdd) {
        addPlayTime(game, time);
        addAcc(game, acc);
        addPoints(game, pointsToAdd);
        Number[] lastGame = new Number[statsTypes];
        lastGame[POINTS - 100] = pointsToAdd;
        lastGame[ACC - 100] = acc;
        lastGame[TIME - 100] = time;
        if (lastGames[game].size() < 5) {
            lastGames[game].add(lastGame);
        } else {
            Number[][] last = (Number[][]) lastGames[game].toArray();
            last[0] = last[1];
            last[1] = last[2];
            last[2] = last[3];
            last[3] = last[4];
            last[4] = lastGame;
            lastGames[game] = Arrays.asList(last);
        }
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }
}
