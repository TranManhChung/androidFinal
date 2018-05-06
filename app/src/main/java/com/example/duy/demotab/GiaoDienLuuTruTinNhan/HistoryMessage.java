package com.example.duy.demotab.GiaoDienLuuTruTinNhan;

import com.example.duy.demotab.GiaoDienChat.Message;

/**
 * Created by Tran Manh Chung on 4/23/2018.
 */

public class HistoryMessage extends Message {

    private String time;

    public HistoryMessage(int avatar, String id, String name, String message,String time) {
        super(avatar, id, name, message);
        this.time=time;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
