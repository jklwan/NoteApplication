package com.chends.note.utils;

import android.app.Application;
import android.content.Context;

/**
 * App util
 */
public class AppUtil {
    private static final AppUtil ourInstance = new AppUtil();
    private Application app;

    public static AppUtil getInstance() {
        return ourInstance;
    }

    private AppUtil() {
    }

    public void setApp(Application app) {
        this.app = app;
    }

    public Application getApp() {
        return app;
    }

    public Context getContext() {
        return app.getApplicationContext();
    }
}

