package com.jason.trip.app;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.orhanobut.logger.AndroidLogTool;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:TripApplication
 * Description:
 * Created by Jason on 16/9/5.
 */
public class TripApplication extends Application {
    private static TripApplication sInstance;

    public TripApplication() {
        sInstance = this;
    }

    /**
     * @deprecated This is hacky.
     */
    public static TripApplication getInstance() {
        return sInstance;
    }

    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        Fresco.initialize(getApplicationContext());
        AndroidThreeTen.init(this);


        Logger
                .init()                         // default PRETTYLOGGER or use just init()
                .methodCount(3)                 // default 2
                .hideThreadInfo()               // default shown
                .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                .methodOffset(2)                // default 0
                .logTool(new AndroidLogTool()); // custom log tool, optional

    }


}
