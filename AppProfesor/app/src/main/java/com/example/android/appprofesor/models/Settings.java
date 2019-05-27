package com.example.android.appprofesor.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Define la tabla Settings en la base de datos interna de la aplicación.
 * Guarda la dirección y el puerto del servidor al que conectarse
 */
@Entity(tableName = "settings")
public class Settings {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private String address;
    @NonNull
    private int port;

    public Settings(@NonNull String address, int port) {
        this.address = address;
        this.port = port;
    }

    public Settings() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
