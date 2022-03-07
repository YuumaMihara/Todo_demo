package com.example.to_do_demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.fragment.app.DialogFragment;

public class ChangeTimeDialog extends DialogFragment {

    private static int id;

    ChangeTimeDialog(int id){
        this.id = id;
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

                list_display list_display = (list_display) getActivity();
                list_display.updateDataTime(id, timeOfDay, timeOfDayInt);
                list_display.readData();
            }
        }).setView(view);
        return builder.create();
    }
}

