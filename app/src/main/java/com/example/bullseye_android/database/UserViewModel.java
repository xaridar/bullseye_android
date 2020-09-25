package com.example.roomtut.users;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.roomtut.words.Word;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private LiveData<List<User>> users;

    public UserViewModel (@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        users = userRepository.getAllUsers();
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }

    public void insert(User user) {
        userRepository.insert(user);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public void remove(User user) {
        userRepository.remove(user);
    }

    public void update(User user) {
        userRepository.update(user);
    }
}
