package com.example.to_do_demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.ConditionVariable;

import androidx.fragment.app.DialogFragment;

public class DeleteDialog extends DialogFragment {

    private static int pos;
    private static Context context;
    private static String dateTxt;
    private static Callback mCall;


    DeleteDialog(int id, Context context,String dateTxt){
        this.pos = id;
        this.context = context;
        this.dateTxt = dateTxt;
    }

    public interface Callback{
        public void OnResultOk();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("確認")
                .setMessage("予定を削除しますか")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbManager db = new dbManager(context, dateTxt);
                        db.deleteData(pos);
                        mCall.OnResultOk();
                    }
                })
                .setNegativeButton("キャンセル", null)
                .setNeutralButton("あとで", null);
        return builder.create();
    }

    void setCallback(Callback call){
        this.mCall = call;
    }
}
