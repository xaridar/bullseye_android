package com.example.bullseye_android.database.user;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public static long[] fromStringLong(String json) {
        Type listType = new TypeToken<long[]>() {}.getType();
        return new Gson().fromJson(json, listType);
    }
    @TypeConverter
    public static int[] fromStringInt(String json) {
        Type listType = new TypeToken<int[]>() {}.getType();
        return new Gson().fromJson(json, listType);
    }
    @TypeConverter
    public static float[] fromStringFloat(String json) {
        Type listType = new TypeToken<float[]>() {}.getType();
        return new Gson().fromJson(json, listType);
    }
    @TypeConverter
    public static List<Number[]>[] fromString(String json) {
        Type listType = new TypeToken<List<Number[]>[]>() {}.getType();
        return new Gson().fromJson(json, listType);
    }
    @TypeConverter
    public static String toStringLong(long[] value) {
        return new Gson().toJson(value);
    }
    @TypeConverter
    public static String toStringInt(int[] value) {
        return new Gson().toJson(value);
    }
    @TypeConverter
    public static String toStringFloat(float[] value) {
        return new Gson().toJson(value);
    }
    @TypeConverter
    public static String toString(List<Number[]>[] value) {
        return new Gson().toJson(value);
    }
}
