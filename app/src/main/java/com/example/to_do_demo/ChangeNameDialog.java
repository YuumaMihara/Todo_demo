package com.example.to_do_demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.fragment.app.DialogFragment;

public class ChangeNameDialog extends DialogFragment {
    private static int id;
    private static Context context;
    private static String dateTxt;
    Callback mCall;

    ChangeNameDialog(int id, Context context, String dateTxt){
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
        View view = inflater.inflate(R.layout.change_name_picker, null, false);

        EditText todoName = view.findViewById(R.id.todo_name1);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("予定名を変更");
        builder.setPositiveButton("予定名を変更", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbManager db = new dbManager(context, dateTxt);
                db.updateDataName(id, todoName.getText().toString());
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
