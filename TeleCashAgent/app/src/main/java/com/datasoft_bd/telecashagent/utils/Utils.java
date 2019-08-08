package com.datasoft_bd.telecashagent.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by noman on 4/4/19.
 */

public class Utils {

    public static void showProgressDialog(ProgressDialog progressDialog, boolean isShowProgressDialog) {
        if (isShowProgressDialog) {
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
    }

    public static void dismissProgressDialog(ProgressDialog progressDialog, boolean isShowProgressDialog) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static void showToast(Context context, String msg, Boolean isError) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = toast.getView();
        if (isError) {
            view.setBackgroundColor(Color.parseColor("#d73030"));
        } else {
            view.setBackgroundColor(Color.parseColor("#2962ff"));
        }
        view.setPadding(10, 0, 10, 0);
        TextView v = view.findViewById(android.R.id.message);
        v.setTextColor(Color.WHITE);
        toast.show();
    }

}
