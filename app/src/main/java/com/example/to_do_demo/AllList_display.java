package com.example.to_do_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AllList_display extends AppCompatActivity {

    private final String TABLE_NAME = "dateItem";
    private String searchTxt;
    private String selection;
    private String[] args;

    private Toolbar bar;

    private SQLiteDatabase db;
    private OpenHelper helper;
    private dbManager dbM;

    private boolean ASC = true;

    private Button mSortBtn, mSearchBtn, mDateBtn;
    private ListView dateList, timeList, nameList;

    private Cursor cursor = null;

    private ArrayAdapter<String> adapter_date;
    private ArrayAdapter<String> adapter_timeOfDay;
    private ArrayAdapter<String> adapter_list;

    private List<String> dayList = new ArrayList<String>();
    private List<String> timeOfDayList = new ArrayList<String>();
    private List<String> dataList = new ArrayList<String>();

    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_list_display);

        mSortBtn = findViewById(R.id.mBtnFilter_sort);
        mDateBtn = findViewById(R.id.mBtnFilter_date);
        mSearchBtn = findViewById(R.id.mBtnFilter_search);

        dateList = findViewById(R.id.dateList);
        timeList = findViewById(R.id.timeList);
        nameList = findViewById(R.id.nameList);

        bar = findViewById(R.id.my_toolbar2);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setAdapter();

        setActivity();

        mSortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ASC){
                    readDB("date DESC, time_int DESC");
                }else{
                    readDB("date ASC, time_int ASC");
                }
                ASC = !ASC;
            }
        });

        mDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SetPeriodDialog spd = new SetPeriodDialog();
                SetPeriodDialog.Callback call = new SetPeriodDialog.Callback() {
                    @Override
                    public void OnResultOk(String startTxt, String endTxt) {
                        selection = "date >= ? and date <= ?";
                        args = new String[]{startTxt, endTxt};
                        readDB(null);
                    }
                };
                spd.setCallback(call);
                spd.show(getSupportFragmentManager(), "dialog");
            }
        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = new EditText(AllList_display.this);
                editText.setHint("");
                new AlertDialog.Builder(AllList_display.this)
                        .setTitle("予定を検索")
                        .setView(editText)
                        .setPositiveButton("検索", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                searchTxt = String.valueOf(editText.getText());
                                selection = "name like ?";
                                args = new String[]{"%" + searchTxt + "%"};
                                readDB(null);
                            }
                        }).show();
            }
        });
    }

    void readDB(String order_by){

        cursor = db.query(
                TABLE_NAME, new String[]{"_id","date", "time", "name", "time_int"},
                selection,args, null, null, order_by);

        listClear();

        dbM.readData(cursor);

        dayList = dbM.dateList;
        timeOfDayList = dbM.timeOfDayList;
        dataList = dbM.dataList;
        setAdapter();
    }

    public void listClear(){
        dayList.clear();
        dataList.clear();
        timeOfDayList.clear();
        adapter_date.notifyDataSetChanged();
        adapter_list.notifyDataSetChanged();
        adapter_timeOfDay.notifyDataSetChanged();
    }

    public void setAdapter(){
        adapter_date = new ArrayAdapter(this,android.R.layout.simple_list_item_1, dayList);
        dateList.setAdapter(adapter_date);

        adapter_timeOfDay = new ArrayAdapter(this,android.R.layout.simple_list_item_1, timeOfDayList);
        timeList.setAdapter(adapter_timeOfDay);

        adapter_list = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataList);
        nameList.setAdapter(adapter_list);
    }

    private void setActivity(){
        if(helper == null){
            helper = new OpenHelper(getApplicationContext());
        }
        if(db == null){
            db = helper.getWritableDatabase();
        }

        dbM = new dbManager(this, null);
        readDB("date ASC, time_int ASC");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();

        return super.onSupportNavigateUp();
    }
}