package com.example.to_do_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class list_display extends AppCompatActivity {

    private Intent intent;
    private Toolbar bar;
    private FloatingActionButton fab;

    private final String TABLE_NAME = "dateItem";

    public SQLiteDatabase db;
    public OpenHelper helper;

    public static String dateTxt;

    public static ArrayAdapter<String> adapter_timeOfDay;
    public static ArrayAdapter<String> adapter_list;

    public static List<Integer> idList = new ArrayList<Integer>();
    public static List<String> timeOfDayList = new ArrayList<String>();
    public static List<String> dataList = new ArrayList<String>();

    public static ListView TimeList;
    public static ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_display);

        //選択された日付の受け取り
        intent = getIntent();
        dateTxt = intent.getStringExtra(MainActivity.SELECTED_DATE);

        TimeList = findViewById(R.id.timeList);
        listView = findViewById(R.id.nameList);
        fab = findViewById(R.id.fab);
        bar = findViewById(R.id.my_toolbar);
        setSupportActionBar(bar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setAdapter();

        readData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TimeSetDialog dialog = new TimeSetDialog();
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        TimeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final ChangeTimeDialog ctd = new ChangeTimeDialog(i);
                ctd.show(getSupportFragmentManager(), "dialog");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final ChangeNameDialog cnd = new ChangeNameDialog(i);
                cnd.show(getSupportFragmentManager(), "dialog");
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final DeleteDialog deleteDialog = new DeleteDialog(i);
                deleteDialog.show(getSupportFragmentManager(), "dialog");
                return true;
            }
        });
    }

    void saveData(String time, String text, String time_int){

        ContentValues values = new ContentValues();
        values.put("date", dateTxt);
        values.put("time", time);
        values.put("name", text);
        values.put("time_int", time_int);

        db.insert(TABLE_NAME, null, values);
    }

    void readData(){

        if(helper == null){
            helper = new OpenHelper(getApplicationContext());
        }
        if(db == null){
            db = helper.getWritableDatabase();
        }

        Cursor cursor = null;

        String order_by = "time_int ASC";

        cursor = db.query(
                TABLE_NAME, new String[]{"_id", "time", "name", "time_int"},
                "date == ?",new String[] {dateTxt}, null, null, order_by);

        listClear();

        if(cursor != null && cursor.moveToFirst()){
            for(int i = 0;i < cursor.getCount();i++) {
                idList.add(cursor.getInt(0));                //db検索用id
                adapter_timeOfDay.add(cursor.getString(1));     //時刻
                adapter_list.add(cursor.getString(2));          //やること名

                cursor.moveToNext();
            }
        }else{
            Log.d("debug", "resolveDb: null");
        }

        cursor.close();
    }

    void updateDataTime(int pos, String dateTime, String dateTimeInt){
        timeOfDayList.set(pos, dateTime);
        adapter_timeOfDay.notifyDataSetChanged();

        String sql = "update " + TABLE_NAME + " set "
                + "time='" + dateTime + "',time_int=" + dateTimeInt
                + " where _id=" + idList.get(pos) + ";";

        try {
            db.execSQL(sql);
        } catch (SQLException e) {
            Log.e("ERROR", e.toString());
        }
    }

    void updateDataName(int pos, String name){
        dataList.set(pos, name);
        adapter_list.notifyDataSetChanged();

        String sql = "update " + TABLE_NAME + " set "
                + "name='" + name
                + "' where _id=" + idList.get(pos) + ";";

        try {
            db.execSQL(sql);
        } catch (SQLException e) {
            Log.e("ERROR", e.toString());
        }
    }

    void deleteData(int pos){
        try{
            db.delete(TABLE_NAME, "_id=" + idList.get(pos), null);
        }  catch (SQLException e) {
            Log.e("ERROR", e.toString());
        }

    }

    public void setAdapter(){
        adapter_list = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter_list);

        adapter_timeOfDay = new ArrayAdapter(this,android.R.layout.simple_list_item_1, timeOfDayList);
        TimeList.setAdapter(adapter_timeOfDay);
    }

    public void listClear(){
        dataList.clear();
        timeOfDayList.clear();
        idList.clear();
        adapter_list.notifyDataSetChanged();
        adapter_timeOfDay.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();

        return super.onSupportNavigateUp();
    }

}