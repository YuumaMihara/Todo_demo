package com.example.to_do_demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import java.util.Set;

public class SetPeriodDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Button mBtnStart,mBtnEnd;

    String dateTxt,startTxt,endTxt;

    boolean start = true;

    Callback mCall;

    public interface Callback{
        public void OnResultOk(String startTxt, String endTxt);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_get_period, null, false);

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.activity_get_period);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("表示する期間を指定");

        mBtnStart = (Button) view.findViewById(R.id.mBtnStart);
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
                start = true;
            }
        });

        mBtnEnd = (Button) view.findViewById(R.id.mBtnEnd);
        mBtnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
                start = false;
            }
        });

        builder.setPositiveButton("決定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCall.OnResultOk(startTxt, endTxt);
            }
        }).setView(view);
        return builder.create();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePick(this);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dateTxt = year + "/" + String.format("%02d",month+1) + "/" + String.format("%02d",dayOfMonth);
        setTxt();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        start = true;
    }

    void setTxt() {
        if(start){
            startTxt = dateTxt;
        }else{
            endTxt = dateTxt;
        }
        System.out.println("start" + startTxt);
        System.out.println("end" + endTxt);
    }

    void setCallback(Callback call){
        this.mCall = call;
    }
}
