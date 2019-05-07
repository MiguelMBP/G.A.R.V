package com.example.android.appprofesor.data;

import com.example.android.appprofesor.models.Settings;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SettingsDAO {

    @Query("SELECT * FROM settings")
    LiveData<List<Settings>> getAll();

    @Insert
    long insertSettings(Settings settings);

    @Update
    int updateSettings(Settings settings);
}
