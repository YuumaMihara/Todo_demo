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

public class ChangeNameDialog extends DialogFragment {
    private static int id;

    ChangeNameDialog(int id){
        this.id = id;
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
                list_display list_display = (list_display) getActivity();
                list_display.updateDataName(id, todoName.getText().toString());
                list_display.readData();
            }
        });
        builder.setView(view);
        return builder.create();
    }
}
