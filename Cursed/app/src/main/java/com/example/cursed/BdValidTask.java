package com.example.cursed;

import android.os.AsyncTask;
import android.util.Log;

public class BdValidTask extends AsyncTask<String, Void, String> {
    User user;
    AppDatabase bd;
    boolean validate;
    AsyncValidate delegate = null;

    public BdValidTask(AppDatabase bd, User user) {
        this.user = user;
        this.bd = bd;
    }

    protected String doInBackground(String... strings) {
        final UserDao userDao = bd.userDao();
        try {
            User user2 = userDao.findByName(user.UserLogin);
            if (user2.UserPassword.equals(user.UserPassword)) validate = true;
        } catch (NullPointerException e) {
            Log.d("tas", "null");
        }
        return null;
    }

    protected void onPostExecute(String result) {
        delegate.processFinish(validate);
    }
}
