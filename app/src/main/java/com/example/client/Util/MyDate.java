package com.example.client.Util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.widget.Toast;

import com.example.client.Callback.DatePickerCallback;

import java.util.Calendar;

public class MyDate {

    public static void showDatePickerDialog(Activity activity, DatePickerCallback callback) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(
                activity,
                (view, year1, month1, dayOfMonth) -> {
                    // Month is 0-based, so you need to add 1
                    calendar.set(year1, month1, dayOfMonth, 0, 0, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                   long epochTime = calendar.getTimeInMillis() / 1000;
                    callback.onDateSelected(epochTime);

                },
                year, month, day
        );
        datePickerDialog.show();
    }
}

