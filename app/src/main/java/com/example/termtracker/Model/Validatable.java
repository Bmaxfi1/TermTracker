package com.example.termtracker.Model;

import android.content.Context;

public interface Validatable {
    boolean isValid(Context context);  //adding a required context gives me the ability to display custom toasts
}
