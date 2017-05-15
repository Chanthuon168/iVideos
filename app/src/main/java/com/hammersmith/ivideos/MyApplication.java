package com.hammersmith.ivideos;

import android.app.Application;
import android.content.Context;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialModule;

/**
 * Created by Chan Thuon on 3/8/2017.
 */
public class MyApplication extends Application {
    public static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication mInstance;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Iconify
                .with(new MaterialModule());
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

}
