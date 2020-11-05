package com.example.bullseye_android.database.user;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserRepository {

    private UserDao userDao;
    private LiveData<List<User>> users;
    private LiveData<Map<Long, User>> usersIDs;
    private ExecutorService service;

    UserRepository (Application application) {
        UserRoomDatabase db = UserRoomDatabase.getInstance(application);
        userDao = db.userDao();
        users = userDao.getAll();
        service = Executors.newFixedThreadPool(100);
    }

    public LiveData<List<User>> getAllUsers() {
        return users;
    }

    public long insert(User user) {
        Future<Long> id = service.submit(new insertAsyncTask(userDao, user));
        try {
            return id.get();
        } catch (ExecutionException | InterruptedException exc) {
            exc.printStackTrace();
            return -1;
        }
    }

    public void remove(User user) {
        service.submit(new removeAsyncTask(userDao, user));
    }

    public void update(User user) {
        service.submit(new updateAsyncTask(userDao, user));
    }

    public void deleteAll() {
        service.submit(new clearAsyncTask(userDao));
    }

    public User getAdmin() {
        Future<User> user = service.submit(new getAdminAsyncTask(userDao));
        try {
            return user.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<User> getUser(long id) {
        Future<LiveData<User>> user = service.submit(new getUserAsyncTask(userDao, id));
        try {
            return user.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class insertAsyncTask implements Callable<Long> {

        private UserDao mAsyncTaskDao;
        private User user;

        insertAsyncTask(UserDao dao, User user) {
            mAsyncTaskDao = dao;
            this.user = user;
        }

        @Override
        public Long call() {
            return mAsyncTaskDao.insert(user);
        }
    }

    public static class clearAsyncTask implements Callable<Void> {

        private final UserDao mAsyncTaskDao;

        clearAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        public Void call() {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public static class removeAsyncTask implements Callable<Void> {

        private UserDao mAsyncTaskDao;
        private User user;

        removeAsyncTask(UserDao dao, User user) {
            mAsyncTaskDao = dao;
            this.user = user;
        }

        @Override
        public Void call() {
            mAsyncTaskDao.delete(user);
            return null;
        }
    }

    public static class updateAsyncTask implements Callable<Void> {

        private UserDao mAsyncTaskDao;
        private User user;

        updateAsyncTask(UserDao dao, User user) {
            mAsyncTaskDao = dao;
            this.user = user;
        }

        @Override
        public Void call() {
            mAsyncTaskDao.update(user);
            return null;
        }
    }

    private static class getUserAsyncTask implements Callable<LiveData<User>> {

        private UserDao mAsyncTaskDao;
        private long id;

        public getUserAsyncTask(UserDao dao, long id) {
            mAsyncTaskDao = dao;
            this.id = id;
        }


        @Override
        public LiveData<User> call() {
            return mAsyncTaskDao.getUser(id);
        }
    }


    private static class getAdminAsyncTask implements Callable<User> {

        private UserDao mAsyncTaskDao;

        public getAdminAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        public User call() {
            return mAsyncTaskDao.getAdmin(true);
        }
    }
}
