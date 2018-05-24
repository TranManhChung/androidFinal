package com.example.duy.demotab.Storage;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by mac on 4/25/18.
 */

@Entity
public class User {
    @PrimaryKey @NonNull
    public final String id;
    public String name;
    public int age;
    public int sex;
    public String avatar;
    public int isOnline;

    public User(String id, String name, int age, int sex, String avatar, int isOnline) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.avatar = avatar;
        this.isOnline = isOnline;
    }
}
