package com.jason.trip.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:DataBaseHelper
 * Description:
 * Created by Jason on 16/11/29.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Trip";
    private static final String DB_PASSWORD = "Trip";
    private static int DB_VERSION = 1;
    private Context context;

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String locationSql="create table if not exist location" +"("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT"+
                "area VARCHAR"+
                "postCode VARCHAR"+")";
        db.execSQL(locationSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropLocationSql="drop table if exist location";
        db.execSQL(dropLocationSql);
        this.onCreate(db);
    }


}
