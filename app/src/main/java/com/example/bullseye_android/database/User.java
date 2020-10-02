package com.example.bullseye_android.database;

import android.util.Log;

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
    public static final int GAME_SORTING_FAST = 5;

    public static final int POINTS = 0;
    public static final int ACC = 1;
    public static final int TIME = 2;
    public int statsTypes = 3;

    public static final int MAX_VOLUME = 200;

    public static final List<String> avatars = new ArrayList<>(Arrays.asList("archer", "boy", "default", "girl", "logo", "logo_alt"));

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

    @Nullable
    @ColumnInfo(name = "avatar")
    private String avatar;

    @ColumnInfo(name = "musicVolume")
    private int musicVolume;

    @ColumnInfo(name = "gameVolume")
    private int gameVolume;

    @ColumnInfo(name = "highScores")
    private long[] highScores;


    public User(@NonNull String name, long id, @Nullable String avatar) {
        this.name = name;
        this.admin = false;
        this.id = id;
        email = null;
        password = null;
        playTime = new long[] {0, 0, 0, 0, 0, 0};
        accuracy = new float[]{0, 0, 0, 0, 0, 0};
        focusPoints = new int[] {0, 0, 0, 0, 0, 0};
        gamesPlayed = new int[] {0, 0, 0, 0, 0, 0};
        highScores = new long[] {0, 5999, 5999, 5999, 0, 0};
        lastGames = new ArrayList[]{new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>(), new ArrayList<>()};
        this.avatar = avatar;
        if (avatar == null) {
            this.avatar = "default";
        }
        musicVolume = 100;
        gameVolume = 100;
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
        lastGame[POINTS] = pointsToAdd;
        lastGame[ACC] = acc;
        lastGame[TIME] = time;
        Log.i("HH", lastGames[game].size()+"");
        if (lastGames[game].size() < 5) {
            lastGames[game].add(lastGame);
        } else {
            List<Number[]> lastG = lastGames[game];
            lastG.set(0, lastG.get(1));
            lastG.set(1, lastG.get(2));
            lastG.set(2, lastG.get(3));
            lastG.set(3, lastG.get(4));
            lastG.set(0, lastGame);
            lastGames[game] = lastG;
        }
        if (game >= 1 && game <= 3) {
            if (time < highScores[game]) {
                highScores[game] = time;
            }
        } else if (game >= 4 && game <= 5) {
            if (time > highScores[game]) {
                highScores[game] = time;
            }
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

    @Nullable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(@Nullable String avatar) {
        if (avatars.contains(avatar)) {
            this.avatar = avatar;
        } else {
            this.avatar = "default";
        }
    }

    public int getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(int volume) {
        if (volume < 0) {
            this.musicVolume = 0;
        } else this.musicVolume = Math.min(volume, MAX_VOLUME);
    }

    public int getGameVolume() {
        return gameVolume;
    }

    public void setGameVolume(int volume) {
        if (volume < 0) {
            this.gameVolume = 0;
        } else this.gameVolume = Math.min(volume, MAX_VOLUME);
    }

    public long[] getHighScores() {
        return highScores;
    }

    public void setHighScores(long[] highScores) {
        this.highScores = highScores;
    }
}
