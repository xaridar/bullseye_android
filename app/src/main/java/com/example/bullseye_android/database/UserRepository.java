package com.example.roomtut.users;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class UserRepository {

    private UserDao userDao;
    private LiveData<List<User>> users;

    UserRepository (Application application) {
        UserRoomDatabase db = UserRoomDatabase.getInstance(application);
        userDao = db.userDao();
        users = userDao.getAll();
    }

    public LiveData<List<User>> getAllUsers() { return users; }

    public void insert(User user) {
        new insertAsyncTask(userDao).execute(user);
    }

    public void remove(User user) {
        new removeAsyncTask(userDao).execute(user);
    }

    public void update(User user) {
        new updateAsyncTask(userDao).execute(user);
    }

    public void deleteAll() {
        new clearAsyncTask(userDao).execute();
    }

    public static class insertAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;

        insertAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            mAsyncTaskDao.insert(users[0]);
            return null;
        }
    }

    public static class clearAsyncTask extends AsyncTask<Void, Void, Void> {

        private final UserDao mAsyncTaskDao;

        public clearAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public static class removeAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;

        removeAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            mAsyncTaskDao.delete(users[0]);
            return null;
        }
    }

    public static class updateAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;

        updateAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            mAsyncTaskDao.update(users[0]);
            return null;
        }
    }
}
