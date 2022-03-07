package com.example.to_do_demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class DeleteDialog extends DialogFragment {

    private static int pos;

    DeleteDialog(int id){
        this.pos = id;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("確認")
                .setMessage("予定を削除しますか")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        list_display list_display = (list_display) getActivity();
                        list_display.deleteData(pos);
                        list_display.readData();
                    }
                })
                .setNegativeButton("キャンセル", null)
                .setNeutralButton("あとで", null);
        return builder.create();
    }
}
