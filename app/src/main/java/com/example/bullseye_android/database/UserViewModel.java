package com.example.bullseye_android.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Flowable;

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

    public User getAdmin() {
        return userRepository.getAdmin();
    }

    public User getUser(long id) {
        return userRepository.getUser(id);
    }
}
