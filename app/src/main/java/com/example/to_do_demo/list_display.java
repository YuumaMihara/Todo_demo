package com.example.to_do_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class list_display extends AppCompatActivity {

    private final String TABLE_NAME = "dateItem";

    private Intent intent;
    private Toolbar bar;
    private FloatingActionButton fab;

    private Cursor cursor = null;

    private String dateTxt;

    private ArrayAdapter<String> adapter_timeOfDay;
    private ArrayAdapter<String> adapter_list;

    private List<Integer> idList = new ArrayList<Integer>();
    private List<String> timeOfDayList = new ArrayList<String>();
    private List<String> dataList = new ArrayList<String>();

    private ListView TimeList;
    private ListView listView;

    private SQLiteDatabase db;
    private OpenHelper helper;
    private dbManager dbM;

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

        if(helper == null){
            helper = new OpenHelper(getApplicationContext());
        }
        if(db == null){
            db = helper.getWritableDatabase();
        }

        dbM = new dbManager(this, dateTxt);
        readDB();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TimeSetDialog dialog = new TimeSetDialog(getApplicationContext(), dateTxt);
                TimeSetDialog.Callback call = new TimeSetDialog.Callback() {
                    @Override
                    public void OnResultOk() {
                        readDB();
                    }
                };
                dialog.setCallback(call);
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        TimeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final ChangeTimeDialog ctd = new ChangeTimeDialog(i, getApplicationContext(), dateTxt);
                ChangeTimeDialog.Callback call = new ChangeTimeDialog.Callback() {
                   @Override
                    public void OnResultOk() {
                         readDB();
                    }
                };
                ctd.setCallback(call);
                ctd.show(getSupportFragmentManager(), "dialog");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final ChangeNameDialog cnd = new ChangeNameDialog(i,getApplicationContext(), dateTxt);
                ChangeNameDialog.Callback call = new ChangeNameDialog.Callback() {
                    @Override
                    public void OnResultOk() {
                        readDB();
                    }
                };
                cnd.setCallback(call);
                cnd.show(getSupportFragmentManager(), "dialog");

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final DeleteDialog deleteDialog = new DeleteDialog(i, getApplicationContext(), dateTxt);
                DeleteDialog.Callback call = new DeleteDialog.Callback() {
                    @Override
                    public void OnResultOk() {
                        readDB();
                    }
                };
                deleteDialog.setCallback(call);
                deleteDialog.show(getSupportFragmentManager(), "dialog");
                return true;
            }
        });
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

    void readDB(){

        String order_by = "time_int ASC";

        cursor = db.query(
                TABLE_NAME, new String[]{"_id","date", "time", "name", "time_int"},
                "date == ?",new String[] {dateTxt}, null, null, order_by);

        listClear();

        dbM.readData(cursor);

        idList = dbM.idList;
        timeOfDayList = dbM.timeOfDayList;
        dataList = dbM.dataList;
        setAdapter();
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_OK);
        finish();

        return super.onSupportNavigateUp();
    }

}