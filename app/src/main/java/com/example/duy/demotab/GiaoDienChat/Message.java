package com.example.duy.demotab.GiaoDienChat;

import com.example.duy.demotab.Other.BasicInfomation;

/**
 * Created by Tran Manh Chung on 4/23/2018.
 */

public class Message extends BasicInfomation{
    private String message;
    private boolean type;

    public Message(int avatar, String id, String name,String message,boolean type) {
        super(avatar, id, name);
        this.message=message;
        this.type=type;
    }

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
