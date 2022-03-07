package com.example.to_do_demo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class OpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "DBList";
    private static final String TABLE_LIST = "dbList";
    private static final String TABLE_DATA_ITEM = "dateItem";
    private static final String _ID = "_id";
    private static final String COLUMN_DATA = "date";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_NAME_LIST = "name";
    private static final String COLUMN_TIME_INT = "time_int";

    //テーブル作成文
    private static final String SQL_CREATE_LIST =
            "CREATE TABLE " + TABLE_LIST + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_LIST + " TEXT," +
                    COLUMN_TIME + " TEXT)";

    private static final String SQL_CREATE_DATA =
            "CREATE TABLE " + TABLE_DATA_ITEM + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_DATA + " TEXT," +           //日
                    COLUMN_TIME + " INTEGER," +        //時刻
                    COLUMN_NAME_LIST + " TEXT," +      //やること名
                    COLUMN_TIME_INT + " TEXT)";     //整列用時刻数値


    OpenHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // テーブル作成
        // SQLiteファイルがなければSQLiteファイルが作成される
        try {
            db.execSQL(SQL_CREATE_LIST);
            db.execSQL(SQL_CREATE_DATA);
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.d("debug", "onCreate(SQLiteDatabase db)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // アップデートの判別
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA_ITEM);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
