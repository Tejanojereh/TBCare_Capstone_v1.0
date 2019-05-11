package com.example.android.tbcare_capstone.Class.Utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionClass {

    public static void Save(Context ctx, String name, String value){
        SharedPreferences s = ctx.getSharedPreferences("session", ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        editor.putString(name, value);
        editor.apply();
    }

    public static String Read(Context ctx, String name, String value){
        SharedPreferences s = ctx.getSharedPreferences("session", ctx.MODE_PRIVATE);
        return s.getString(name,value);
    }
}
