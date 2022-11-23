package com.example.cursed;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User {
    @ColumnInfo(name = "login")
    @PrimaryKey
    @NonNull
    public String UserLogin;
    @ColumnInfo(name = "password")
    public String UserPassword;

    public User(String UserLogin, String UserPassword) {
        this.UserLogin = UserLogin;
        this.UserPassword = UserPassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "UserLogin='" + UserLogin + '\'' +
                ", UserPassword='" + UserPassword + '\'' +
                '}';
    }
}
