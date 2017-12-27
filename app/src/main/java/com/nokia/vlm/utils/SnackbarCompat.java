package com.nokia.vlm.utils;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;


public class SnackbarCompat {

    public static Snackbar setSnackbarBackgroundColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        view.setBackgroundColor(color);
        return snackbar;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static Snackbar setSnackbarBackground(Snackbar snackbar, Drawable drawable) {
        View view = snackbar.getView();
        view.setBackground(drawable);
        return snackbar;
    }


    public static Snackbar setSnackbarBackgroundResource(Snackbar snackbar, int res) {
        View view = snackbar.getView();
        view.setBackgroundResource(res);
        return snackbar;
    }

    public static Snackbar setSnackbarTextColor(Snackbar snackbar, int color) {
        TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        return snackbar;
    }

    public static Snackbar setSnackbarTextSize(Snackbar snackbar, int size) {
        TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextSize(size);
        return snackbar;
    }

}
