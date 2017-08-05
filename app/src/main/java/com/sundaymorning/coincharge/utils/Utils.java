package com.sundaymorning.coincharge.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.sundaymorning.coincharge.R;

import java.util.regex.Pattern;

/**
 * Created by Dynast on 2017. 6. 25..
 */

public class Utils {

    public static boolean checkNetworkAvailable(Context context) {
        ConnectivityManager conn_manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = false;
        try {
            NetworkInfo network_info = conn_manager.getActiveNetworkInfo();
            if (network_info != null && network_info.isConnected()) {
                if (network_info.getType() == ConnectivityManager.TYPE_WIFI) {
                    // do some staff with WiFi connection
                    isNetworkAvailable = true;
                } else if (network_info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // do something with Carrier connection
                    isNetworkAvailable = true;
                } else if (network_info.getType() == ConnectivityManager.TYPE_WIMAX) {
                    // do something with Wibro/Wimax connection
                    isNetworkAvailable = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isNetworkAvailable;
    }

    public static void showToast(Context context, Object msg) {
        Toast toast = Toast.makeText(context, String.valueOf(msg), Toast.LENGTH_SHORT);
        toast.show();
    }

    public static AlertDialog showDialog(Context context, String title, String string, String okStr, String cancelStr, DialogInterface.OnClickListener listenerOK, DialogInterface.OnClickListener listenerCancel) {
        if (!string.equals("") && !okStr.isEmpty() && (listenerOK != null || listenerCancel != null)) {
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
            alt_bld.setMessage(string);
            alt_bld.setCancelable(false);
            if (okStr != null && listenerOK != null)
                alt_bld.setPositiveButton(okStr, listenerOK);
            if (cancelStr != null && listenerCancel != null && !cancelStr.isEmpty())
                alt_bld.setNegativeButton(cancelStr, listenerCancel);

            AlertDialog alert = alt_bld.create();

            if (title != null && !title.equals(""))
                alert.setTitle(title);
            else
                alert.setTitle(R.string.app_name);

            alert.setIcon(R.mipmap.ic_launcher);
            alert.show();

            return alert;
        }
        return null;
    }

    public static void Log(Object message) {
        Log.e("@@@@@@", message.toString());
    }

    public static boolean checkPatternPhoneNumber(String str) {
        String regex = "^01(?:0|1[6-9])(?:\\d{3}|\\d{4})\\d{4}$";
        return Pattern.matches(regex, str);
    }

    public static boolean checkPatternEmail(String mail) {
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        return Pattern.matches(regex, mail);
    }

    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getResources().getString(R.string.loading));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }

        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    public static void hideProgressDialog(Context context) {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            progressDialog = null;
        }
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
