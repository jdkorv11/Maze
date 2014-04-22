package com.example.maze;

import android.app.Application;
import android.content.Context;

/**
 * Created by Owner on 4/22/14.
 */
public class MyApplication extends Application {

    private static Context context;

    public void onCreate(){
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {

        return MyApplication.context;
    }
}
