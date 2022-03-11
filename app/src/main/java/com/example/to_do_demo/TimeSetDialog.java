package com.example.to_do_demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeSetDialog extends DialogFragment {

    private static Context context;
    private static String dateTxt;
    private static Callback mCall;

    TimeSetDialog(Context context, String dateTxt){
        this.context = context;
        this.dateTxt = dateTxt;
    }

    public interface Callback{
        public void OnResultOk();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.number_picker, null, false);

        NumberPicker numberPickerHours = view.findViewById(R.id.numberPicker_hours);
        numberPickerHours.setMinValue(0);
        numberPickerHours.setMaxValue(23);

        NumberPicker numberPickerMinutes = view.findViewById(R.id.numberPicker_minutes);
        numberPickerMinutes.setMinValue(0);
        numberPickerMinutes.setMaxValue(59);

        EditText todoName = view.findViewById(R.id.todo_name);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("予定をセット");
        builder.setPositiveButton("予定を追加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String timeOfDay = String.valueOf(numberPickerHours.getValue()) + ":"
                        + String.valueOf(numberPickerMinutes.getValue());

                String timeOfDayInt = String.format("%02d", numberPickerHours.getValue())
                        + String.format("%02d", numberPickerMinutes.getValue());

                dbManager db = new dbManager(context, dateTxt);
                db.saveData(timeOfDay, todoName.getText().toString(), timeOfDayInt);
                mCall.OnResultOk();
            }
        });
        builder.setView(view);
        return builder.create();
    }

    void setCallback(Callback call){
        this.mCall = call;
    }
}
