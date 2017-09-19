package com.jdsv650.todo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by james on 9/17/17.
 */

public class ToDoDatabase extends SQLiteOpenHelper {

    static final Integer VERSION = 1;       // db version
    static final String NAME = "ToDoDb";  // db name

    public ToDoDatabase(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override   // create database table/s
    public void onCreate(SQLiteDatabase database) {


        String sql = "CREATE TABLE TODO(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TITLE TEXT, DESCRIPTION TEXT, DATE TEXT, STATUS INTEGER )";
        database.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
