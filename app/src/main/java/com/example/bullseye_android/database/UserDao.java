package com.example.bullseye_android.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * from user_table where admin == :bool")
    User getAdmin(boolean bool);

    @Query("SELECT * from user_table where id == :id")
    LiveData<User> getUser(long id);

    @Update
    void update(User user);

    @Query("SELECT * from user_table")
    LiveData<List<User>> getAll();

    @Query("DELETE from user_table")
    void deleteAll();
}
