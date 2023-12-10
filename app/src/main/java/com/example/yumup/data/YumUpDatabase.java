package com.example.yumup.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DietEntity.class}, version = 1)
public abstract class YumUpDatabase extends RoomDatabase {
    private static YumUpDatabase database = null;
    public abstract DietDao dietDao();

    private static final String DATABASE_NAME = "yum_up_database";

    public synchronized static YumUpDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), YumUpDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
}