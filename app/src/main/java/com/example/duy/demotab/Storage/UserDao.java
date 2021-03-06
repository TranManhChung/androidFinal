package com.example.duy.demotab.Storage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by mac on 4/25/18.
 */

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUser(User user);

    @Query("select * from user")
    public List<User> getAllUser();

    @Query("select * from user where id= :userId")
    public User getUser(String userId);

    @Query("select * from user where isOnline= '1'")
    public List<User> getUserOnline();

    @Query("delete from user")
    public void ClearUser();

    @Update
    public void updateUser(User... users);

}
