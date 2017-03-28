package com.bhimoffline.truedev.bhimoffline.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

/**
 * Created by rahul on 17-Mar-17.
 */

public class MyDialog extends android.support.v4.app.DialogFragment {
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.CALL_PHONE};


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Permission Required")
                .setCancelable(false)
                .setMessage("BHiM Offline doesn't work without phone permission.\nWe just need to make a call to fetch your balance. So please allow this permission.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
                    }
                })
                .setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //return;
                    }
                });
        Dialog a = alertDialog.create();
//        alertDialog.show();
        return a;
    }
}

//we are try
