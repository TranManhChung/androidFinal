package com.example.duy.demotab.Storage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by mac on 4/25/18.
 */
@Database(entities = {User.class, ChatHistory.class}, version = 14, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract ChatHistoryDao chatHistoryDao();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE==null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "userdatabase").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }
    public static void DestroyInstance() {
        INSTANCE = null;
    }
}
