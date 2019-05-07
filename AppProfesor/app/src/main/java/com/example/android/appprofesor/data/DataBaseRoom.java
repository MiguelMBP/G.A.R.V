package com.example.android.appprofesor.data;

import android.content.Context;

import com.example.android.appprofesor.models.Settings;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Settings.class}, version=1, exportSchema = false)
public abstract class DataBaseRoom extends RoomDatabase{
    public abstract SettingsDAO settingsDAO();
    private static DataBaseRoom INSTANCE = null;

    public static DataBaseRoom getInstance(final Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DataBaseRoom.class, "basket_counter.db").fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }
}
