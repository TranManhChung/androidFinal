package com.example.duy.demotab.Storage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 4/29/18.
 */

@Dao
public interface ChatHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChatHistory(ChatHistory chatHistory);

    @Query("select * from chatHistory")
    public List<ChatHistory> getListChatHistory();

    @Query("select * from chatHistory where userId=:userId")
    public ChatHistory getChatHistory(String userId);

    @Delete
    public void clearChat(ChatHistory chatHistory);
}
