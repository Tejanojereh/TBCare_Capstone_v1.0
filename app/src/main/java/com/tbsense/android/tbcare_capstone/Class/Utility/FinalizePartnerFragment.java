package com.tbsense.android.tbcare_capstone.Class.Utility;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.tbsense.android.tbcare_capstone.Class.WebServiceClass;

import org.json.JSONArray;

public class FinalizePartnerFragment extends android.support.v4.app.DialogFragment implements WebServiceClass.Listener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /*String address = "http://tbcarephp.azurewebsites.net/retrieve_partnerList.php";
                        String[] value = {};
                        String[] valueName = {};
                        WebServiceClass wbc = new WebServiceClass(address, value, valueName, (WebServiceClass.Listener) getActivity(), getActivity());

                        wbc.execute();*/
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {

    }
}
