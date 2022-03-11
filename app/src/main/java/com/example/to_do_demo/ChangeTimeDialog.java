package com.example.to_do_demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.fragment.app.DialogFragment;

import java.util.concurrent.CopyOnWriteArrayList;

public class ChangeTimeDialog extends DialogFragment {

    private static int id;
    private static Context context;
    private static String dateTxt;
    Callback mCall;


    ChangeTimeDialog(int id, Context context, String dateTxt){
        this.id = id;
        this.context = context;
        this.dateTxt = dateTxt;
    }

    public interface Callback{
        public void OnResultOk();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.change_number_picker, null, false);

        NumberPicker numberPickerHours = view.findViewById(R.id.numberPicker_hours1);
        numberPickerHours.setMinValue(0);
        numberPickerHours.setMaxValue(23);

        NumberPicker numberPickerMinutes = view.findViewById(R.id.numberPicker_minutes1);
        numberPickerMinutes.setMinValue(0);
        numberPickerMinutes.setMaxValue(59);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("予定時刻を変更");

        builder.setPositiveButton("予定時刻を変更", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String timeOfDay = String.valueOf(numberPickerHours.getValue()) + ":"
                        + String.valueOf(numberPickerMinutes.getValue());

                String timeOfDayInt = String.format("%02d", numberPickerHours.getValue())
                        + String.format("%02d", numberPickerMinutes.getValue());

                dbManager db = new dbManager(context,dateTxt);
                db.updateDataTime(id, timeOfDay, timeOfDayInt);
                mCall.OnResultOk();
            }
        }).setView(view);
        return builder.create();
    }

    void setCallback(Callback call){
        this.mCall = call;
    }
}

