package com.example.duy.demotab.Other;

/**
 * Created by Tran Manh Chung on 4/24/2018.
 */

public class BasicInfomation {
    private int avatar;
    private String id;
    private String name;

    public BasicInfomation(int avatar, String id, String name) {
        this.avatar = avatar;
        this.id = id;
        this.name = name;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
