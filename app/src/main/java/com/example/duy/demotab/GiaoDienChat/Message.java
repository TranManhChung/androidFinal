package com.example.duy.demotab.GiaoDienChat;

import com.example.duy.demotab.Other.BasicInfomation;

/**
 * Created by Tran Manh Chung on 4/23/2018.
 */

public class Message extends BasicInfomation{
    private String message;

    public Message(int avatar, String id, String name,String message) {
        super(avatar, id, name);
        this.message=message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
