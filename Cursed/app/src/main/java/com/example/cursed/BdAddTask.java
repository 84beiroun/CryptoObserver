package com.example.cursed;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

public class BdAddTask extends AsyncTask<String, Void, String> {
    User user;
    AppDatabase bd;
    View view;
    boolean issues = false;

    public BdAddTask(AppDatabase bd, User user, View view) {
        this.user = user;
        this.bd = bd;
        this.view = view;
    }

    @Override
    protected String doInBackground(String... strings) {
        final UserDao userDao = bd.userDao();
        if (!userDao.checkExist(user.UserLogin)) {
            userDao.insertAll(user);
        } else issues = true;
        return null;
    }

    protected void onPostExecute(String result) {
        if (issues)
            Toast.makeText(view.getContext(), "Such user existed", Toast.LENGTH_SHORT).show();

    }
}
