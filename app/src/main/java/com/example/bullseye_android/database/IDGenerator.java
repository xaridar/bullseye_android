package com.example.bullseye_android.database;

import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

public class IDGenerator {

    private static AndroidViewModel mWordViewModel;

    public static IDGenerator INSTANCE;

    public IDGenerator(AndroidViewModel androidViewModel) {
        mWordViewModel = androidViewModel;
    }

    public static IDGenerator getInstance(AndroidViewModel androidViewModel) {
        if (INSTANCE == null) {
            INSTANCE = new IDGenerator(androidViewModel);
        }
        return INSTANCE;
    }

    public long getId() {
        try {
            List<Long> ids = new getIDSAsync().execute().get();
            long id = 0;
            do {
                long randLong = ThreadLocalRandom.current().nextLong(10000000L, 1000000000000L);
                if (!ids.contains(randLong)) {
                    id = randLong;
                }
            } while (id == 0);
            return id;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static class getIDSAsync extends AsyncTask<Void, Void, List<Long>> {

        @Override
        protected List<Long> doInBackground(Void... voids) {
            List<Long> ids = new ArrayList<>();
            List<User> users = ((UserViewModel) mWordViewModel).getUsers().getValue();
            for(User user : users) {
                ids.add(user.getId());
            }
            return ids;
        }
    }

}
