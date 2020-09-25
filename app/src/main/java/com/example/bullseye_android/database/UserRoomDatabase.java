package com.example.roomtut.users;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.roomtut.words.WordDao;

@Database(entities = {User.class}, version = 5, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class UserRoomDatabase extends RoomDatabase {

    public static UserRoomDatabase INSTANCE;

    public abstract UserDao userDao();

    public static UserRoomDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), UserRoomDatabase.class, "user_database")
                            .addMigrations(MIGRATION_1_2)
//                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
        }
    };

}
