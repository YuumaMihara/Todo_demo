package com.example.to_do_demo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class DatePick extends DialogFragment implements
        DatePickerDialog.OnDateSetListener{

    private DatePickerDialog.OnDateSetListener dateSetListener;

    DatePick(DatePickerDialog.OnDateSetListener dateSetListener){
        this.dateSetListener = dateSetListener;
    }
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                dateSetListener, year, month, day);
        return datePickerDialog;
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year,
                          int monthOfYear, int dayOfMonth) {
    }
}
