package com.tim.test01.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = MySQLiteOpenHelper.class.getSimpleName();

    public MySQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate:db=" + db);
        String sqlCreateTableScore="DROP TABLE IF EXISTS score";
        String sqlCreateTableScore1="CREATE TABLE IF NOT EXISTS score(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR ( 20 ), grade INTEGER, time VARCHAR ( 1000 ) )";
        db.execSQL(sqlCreateTableScore);
        db.execSQL(sqlCreateTableScore1);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.i(TAG, "onOpen:db=" + db);

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        Log.i(TAG, "onConfigure:db=" + db);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        Log.i(TAG, "onUpgrade:db=" + db + ",oldVersion:=" + oldVersion + ",newVersion:=" + newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade:db=" + db + ",oldVersion:=" + oldVersion + ",newVersion:=" + newVersion);
    }
}
