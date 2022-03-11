package com.example.to_do_demo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class dbManager{

    public SQLiteDatabase db;
    public OpenHelper helper;

    public boolean update = false;

    private final String TABLE_NAME = "dateItem";

    private static String dateTxt;

    public static List<Integer> idList = new ArrayList<Integer>();
    public static List<String> dateList = new ArrayList<String>();
    public static List<String> timeOfDayList = new ArrayList<String>();
    public static List<String> dataList = new ArrayList<String>();

    protected final Context context;

    dbManager(Context context, String dateTxt){
        this.context = context;
        this.dateTxt = dateTxt;

        if(helper == null){
            helper = new OpenHelper(this.context);
        }
        if(db == null){
            db = helper.getWritableDatabase();
        }
    }

    void saveData(String time, String text, String time_int){

        ContentValues values = new ContentValues();
        values.put("date", dateTxt);
        values.put("time", time);
        values.put("name", text);
        values.put("time_int", time_int);

        db.insert(TABLE_NAME, null, values);
    }

    void readData(Cursor c){

        listClear();

        Cursor cursor = null;

        cursor = c;

        if(cursor != null && cursor.moveToFirst()){
            for(int i = 0;i < cursor.getCount();i++) {
                idList.add(cursor.getInt(0));                   //db検索用id
                dateList.add(cursor.getString(1));          //日付
                timeOfDayList.add(cursor.getString(2));     //時刻
                dataList.add(cursor.getString(3));          //やること名

                cursor.moveToNext();
            }
        }else{
            Log.d("debug", "resolveDb: null");
        }

        cursor.close();
    }

    void updateDataTime(int pos, String dateTime, String dateTimeInt){

        String sql = "update " + TABLE_NAME + " set "
                + "time='" + dateTime + "',time_int=" + dateTimeInt
                + " where _id=" + idList.get(pos) + ";";

        try {
            db.execSQL(sql);
        } catch (SQLException e) {
            Log.e("ERROR", e.toString());
        }
        update = true;
    }

    void updateDataName(int pos, String name){

        String sql = "update " + TABLE_NAME + " set "
                + "name='" + name
                + "' where _id=" + idList.get(pos) + ";";

        try {
            db.execSQL(sql);
        } catch (SQLException e) {
            Log.e("ERROR", e.toString());
        }
        update = true;
    }

    void deleteData(int pos){
        try{
            db.delete(TABLE_NAME, "_id=" + idList.get(pos), null);
        }  catch (SQLException e) {
            Log.e("ERROR", e.toString());
        }
        update = true;
    }

    void listClear(){
        idList.clear();
        dateList.clear();
        timeOfDayList.clear();
        dataList.clear();
    }
}
