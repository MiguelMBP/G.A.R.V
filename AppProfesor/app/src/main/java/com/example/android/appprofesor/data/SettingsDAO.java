package com.example.android.appprofesor.data;

import com.example.android.appprofesor.models.Settings;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Define las consultas que se realizan sobre la tabla de Settings
 */
@Dao
public interface SettingsDAO {

    /**
     * Recoge toda la información de la tabla
     * @return
     */
    @Query("SELECT * FROM settings")
    LiveData<List<Settings>> getAll();

    /**
     * Inserta un registro enla tabla
     * @param settings
     * @return
     */
    @Insert
    long insertSettings(Settings settings);

    /**
     * Actualiza un registro en la tabla
     * @param settings
     * @return
     */
    @Update
    int updateSettings(Settings settings);
}
