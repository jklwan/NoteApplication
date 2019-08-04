package com.chends.note;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;

import com.chends.note.utils.AppUtil;
import com.chends.note.utils.ContextUtil;
import com.chends.note.utils.LanguageUtil;
import com.chends.note.utils.LogUtils;

/**
 * NoteApp
 * @author chends create on 2019/7/23.
 */
public class NoteApp extends Application {

    private boolean isMainProcess = false;

    @Override
    public void onCreate() {
        super.onCreate();
        AppUtil.getInstance().setApp(this);
        LanguageUtil.init(this);
        initProcess();
        if (isMainProcess) {
            registerActivityLifecycleCallbacks(new LifeCycle());
        }
    }

    private void initProcess() {
        // 获取当前包名
        String packageName = getBaseContext().getPackageName();
        // 获取当前进程名
        String processName = ContextUtil.getProcessName(android.os.Process.myPid());
        isMainProcess = processName == null || TextUtils.equals(processName, packageName);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LogUtils.d("onConfigurationChanged:" + newConfig.toString());
        super.onConfigurationChanged(newConfig);
        // 如果是跟随系统需要重新设置
        if (LanguageUtil.onConfigurationChanged()) {
            ActivityStack.getInstance().reCreateAll();
        }
    }

    private class LifeCycle implements Application.ActivityLifecycleCallbacks {
        public LifeCycle() {

        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            ActivityStack.getInstance().add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            ActivityStack.getInstance().setTopActivity(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            ActivityStack.getInstance().saveInstance(activity, outState);
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            ActivityStack.getInstance().remove(activity);
        }
    }
}