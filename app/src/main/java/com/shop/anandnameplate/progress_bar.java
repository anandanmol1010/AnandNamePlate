package com.shop.anandnameplate;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class progress_bar {

    private Activity activity;
    private AlertDialog dialog;

    progress_bar(Activity myActivity) {
        activity = myActivity;
    }

    void startDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_bar,null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog()
    {
        dialog.dismiss();
    }
}
