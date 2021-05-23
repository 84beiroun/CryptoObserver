package com.example.cursed;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();
    @Query("SELECT * FROM user WHERE login LIKE :trylogin LIMIT 1")
    User findByName(String trylogin);
    @Query("SELECT EXISTS(SELECT 1 FROM user WHERE login LIKE :trylogin)")
    boolean checkExist(String trylogin);
    @Insert
    void insertAll(User users);
    @Delete
    void delete(User user);

}
