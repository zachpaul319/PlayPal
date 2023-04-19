package com.example.playpalapp;

import android.content.Context;
import android.widget.Toast;

public class Toaster {
    public static void showToast(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
    }
}
