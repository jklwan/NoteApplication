package com.chends.note;

import android.app.Application;
import android.content.Context;

import com.chends.note.utils.AppUtil;

/**
 * NoteApp
 * @author chends create on 2019/7/23.
 */
public class NoteApp extends Application {


    @Override
    public void onCreate() {
        AppUtil.getInstance().setApp(this);
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}