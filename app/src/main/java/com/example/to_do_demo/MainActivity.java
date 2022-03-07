package com.example.to_do_demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

public class MainActivity extends AppCompatActivity {

    public static final String SELECTED_DATE = "com.example.to_do_demo.DATA";

    private CalendarView calendarView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                //クラス遷移(日付を引数に)
                //引数を元にデータベース探索
                //予定があればリストに追加して表示
                String dateTxt = year + "/" + month + "/" + day;
                Intent intent = new Intent(getApplication(), list_display.class);
                intent.putExtra(SELECTED_DATE, dateTxt);
                startActivity(intent);
            }
        });
    }
}