package com.example.to_do_demo;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener {

    public static final String SELECTED_DATE = "com.example.to_do_demo.DATA";
    private static final int REQUEST_CODE = 1;
    private final String TABLE_NAME = "dateItem";

    private SQLiteDatabase db;
    private OpenHelper helper;
    private dbManager dbM;

    private TextView textView;
    private Button mListBtn;
    private Button mAllListBtn;
    private Intent intent;
    private Cursor cursor = null;

    private String TodayDateTxt;

    private ArrayAdapter<String> adapter_timeOfDay;
    private ArrayAdapter<String> adapter_list;

    private List<String> timeOfDayList = new ArrayList<String>();
    private List<String> dataList = new ArrayList<String>();

    private ListView TimeList;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListBtn = findViewById(R.id.mListButton);
        mAllListBtn = findViewById(R.id.mAllListButton);

        TimeList = findViewById(R.id.timeList);
        listView = findViewById(R.id.nameList);

        textView = findViewById(R.id.textView2);

        setAdapter();

        setActivity();

        mListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        mAllListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), AllList_display.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dateTxt = year + "/" + String.format("%02d",month+1) + "/" + String.format("%02d",dayOfMonth);
        Intent intent = new Intent(getApplication(), list_display.class);
        intent.putExtra(SELECTED_DATE, dateTxt);
        startActivityForResult(intent,REQUEST_CODE);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePick(this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void setAdapter(){
        adapter_list = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter_list);

        adapter_timeOfDay = new ArrayAdapter(this,android.R.layout.simple_list_item_1, timeOfDayList);
        TimeList.setAdapter(adapter_timeOfDay);
    }

    void readDB(){

        String order_by = "time_int ASC";

        cursor = db.query(
                TABLE_NAME, new String[]{"_id","date", "time", "name", "time_int"},
                "date == ?",new String[] {TodayDateTxt}, null, null, order_by);

        listClear();

        dbM.readData(cursor);

        timeOfDayList = dbM.timeOfDayList;
        dataList = dbM.dataList;
        setAdapter();

        textView.setText(timeOfDayList.size() + "件");
    }

    public void listClear(){
        dataList.clear();
        timeOfDayList.clear();
        adapter_list.notifyDataSetChanged();
        adapter_timeOfDay.notifyDataSetChanged();
    }

    private void setActivity(){
        if(helper == null){
            helper = new OpenHelper(getApplicationContext());
        }
        if(db == null){
            db = helper.getWritableDatabase();
        }

        LocalDate date = LocalDate.now();
        TodayDateTxt = date.toString().replace("-", "/");

        dbM = new dbManager(this, TodayDateTxt);
        readDB();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setActivity();
    }
}