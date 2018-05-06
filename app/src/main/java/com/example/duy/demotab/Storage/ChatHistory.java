package com.example.duy.demotab.Storage;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.example.duy.demotab.GiaoDienChat.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by mac on 4/25/18.
 */

@Entity
public class ChatHistory {
    @PrimaryKey @NonNull
    public String userId;
    public String chatLog;
    public String time;

    public ChatHistory(String userId, String chatLog, String time) {
        this.userId = userId;
        this.chatLog = chatLog;
        this.time = time;
    }

    public void insertChatLog(String messages) {
        this.chatLog = messages;
    }


    public ArrayList<Message> getChatLog() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Message>>() {}.getType();
        ArrayList<Message> messages = gson.fromJson(this.chatLog, type);
        return messages;
    }

    public String getLatestMessage() {
        ArrayList<Message> messages = this.getChatLog();
        return messages.get(messages.size()-1).getMessage();
    }
    public String getTime() {
        return this.time;
    }

}
