package com.example.android.tbcare_capstone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

public class NotifyDialog extends AppCompatDialogFragment {

@Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
{
    AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
    builder.setTitle("Reminder:").setMessage("It's time for medicine! Have you taken your medicine?").setPositiveButton("yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }


    }).setNegativeButton("no", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    });

    return builder.create();
}




}
