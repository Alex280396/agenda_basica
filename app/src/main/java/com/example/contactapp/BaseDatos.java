package com.example.contactapp;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BaseDatos extends SQLiteOpenHelper {
    public static final String BASEDEDATOS = "agenda.db";
    public static final String TABLA = "contactos";
    public static final int VER = 1;

    public BaseDatos(@Nullable Context context) {
        super(context, BASEDEDATOS, null, VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + TABLA + "(id integer primary key, imagen blob, nombre text, telefono text)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "drop table if exists " + TABLA + "";
        db.execSQL(query);
    }
}