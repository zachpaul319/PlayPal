package com.example.playpalapp;

import android.content.Context;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class FieldChecker {
    private static boolean emptyField(@NonNull EditText field) {return field.getText().toString().equals("");}

    public static boolean allFieldsFilledOut(@NonNull EditText[] fields) {
        for (EditText field : fields) {
            if (emptyField(field)) return false;
        }
        return true;
    }

    public static void changeBorderColors(@NonNull EditText[] fields) {
        for (EditText field : fields) {
            if (emptyField(field)) {
                field.setBackgroundColor(Color.MAGENTA);
            } else {
                field.setBackgroundColor(Color.WHITE);
            }
        }
    }

    public static void showToast(Context context) {
        Toast toast = Toast.makeText(context, "Make sure all fields are filled out", Toast.LENGTH_LONG);
        toast.show();
    }
}