package com.chends.note;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import com.chends.note.utils.ContextUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * activity栈类
 */
public class ActivityStack {
    private List<Activity> activityList = new ArrayList<>();
    private List<Activity> saveInstance = new ArrayList<>();

    private Activity topActivity = null;

    private ActivityStack() {
    }

    /**
     * 单一实例
     */
    public static ActivityStack getInstance() {
        return SingleApp.instance;
    }

    /**
     * 结束activity
     * @param cls activity
     */
    public void finishActivity(Class<? extends Activity> cls) {
        if (activityList.size() > 0) {
            for (Activity activity : activityList) {
                if (activity.getClass() == cls) {
                    if (ContextUtil.isAlive(activity)) {
                        activity.finish();
                    }
                }
            }
        }

        if (saveInstance.size() > 0) {
            for (Activity activity : saveInstance) {
                if (activity.getClass() == cls) {
                    activity.finish();
                }
            }
        }
    }

    /**
     * 结束多个activity
     * @param array array
     */
    public void finishActivity(Class<? extends Activity>... array) {
        if (array != null && array.length > 0) {
            List<Class<? extends Activity>> list = Arrays.asList(array);

            if (activityList.size() > 0) {
                for (Activity activity : activityList) {
                    if (list.contains(activity.getClass())) {
                        if (ContextUtil.isAlive(activity)) {
                            activity.finish();
                        }
                    }
                }
            }

            if (saveInstance.size() > 0) {
                for (Activity activity : saveInstance) {
                    if (list.contains(activity.getClass())) {
                        activity.finish();
                    }
                }
            }
        }
    }

    public void saveInstance(Activity activity, Bundle outState) {
        if (activity != null) {
            saveInstance.add(activity);
        }
    }

    private static class SingleApp {
        @SuppressLint("StaticFieldLeak")
        private static ActivityStack instance = new ActivityStack();
    }

    public void add(Activity activity) {
        if (activity != null) {
            activityList.add(activity);
        }
    }

    public void clearSaveInstance() {
        saveInstance.clear();
    }

    public void remove(Activity activity) {
        if (activity != null) {
            activityList.remove(activity);
        }
    }

    public void setTopActivity(Activity activity) {
        topActivity = activity;
    }

    public void finishAllActivity() {
        topActivity = null;
        for (Activity activity : activityList) {
            if (ContextUtil.isAlive(activity)) {
                activity.finish();
            }
        }
        activityList.clear();
        for (Activity activity : saveInstance) {
            if (ContextUtil.isAlive(activity)) {
                activity.finish();
            }
        }
        saveInstance.clear();
    }

    public void finishAllActivityExcept(Activity exceptActivity) {
        boolean exist = false;
        for (Activity activity : activityList) {
            if (activity != exceptActivity) {
                activity.finish();
            } else {
                exist = true;
            }
        }
        activityList.clear();
        if (exist) {
            activityList.add(exceptActivity);
        }
    }

    public void finishAllActivityExcept(Class<? extends Activity> exceptActivityCls) {
        Activity exceptActivity = null;
        for (Activity activity : activityList) {
            if (activity.getClass() != exceptActivityCls) {
                activity.finish();
            } else {
                if ((exceptActivity != null) && (exceptActivity != activity)) {
                    exceptActivity.finish();
                }
                exceptActivity = activity;
            }
        }
        activityList.clear();
        if (exceptActivity != null) {
            activityList.add(exceptActivity);
        }
    }

    public Activity getTopActivity() {
        return topActivity;
    }

    public boolean isTop(Activity activity) {
        return activity != null && activity == getTopActivity();
    }

    public int getSize() {
        return activityList.size();
    }

    public Activity getActivity(Class<?> cls) {
        if ((activityList != null) && (cls != null)) {
            for (Activity activity : activityList) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        }
        return null;
    }

    public void reCreateAll() {
        if (activityList.isEmpty()) return;
        for (Activity activity : activityList) {
            activity.recreate();
        }
    }
}
