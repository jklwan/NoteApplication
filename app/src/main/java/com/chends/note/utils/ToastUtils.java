package com.chends.note.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ToastUtils
 */
public class ToastUtils {

    private ToastUtils() {
        throw new AssertionError();
    }

    private static Toast toast;

    /**
     * showShort
     * @param message message
     */
    public static void showShort(final CharSequence message) {
        showShort(AppUtil.getInstance().getContext(), message);
    }

    /**
     * showShort
     * @param context context
     * @param message message
     */
    public static void showShort(Context context, CharSequence message) {
        if (context == null) return;
        realShowShort(context.getApplicationContext(), message);
    }

    /**
     * realShowShort
     * @param context context
     * @param message message
     */
    private static void realShowShort(Context context, CharSequence message) {
        if (context == null) return;
        if (TextUtils.isEmpty(message)) return;
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            ThreadUtils.runOnUIThread(() -> realShow(context, message, Toast.LENGTH_SHORT));
        } else {
            realShow(context, message, Toast.LENGTH_SHORT);
        }
    }

    /**
     * showLong
     * @param message message
     */
    public static void showLong(final CharSequence message) {
        if (TextUtils.isEmpty(message)) return;
        showLong(AppUtil.getInstance().getContext(), message);
    }

    /**
     * showLong
     * @param context context
     * @param message message
     */
    public static void showLong(Context context, CharSequence message) {
        if (context == null) return;
        if (TextUtils.isEmpty(message)) return;
        realShowLong(context.getApplicationContext(), message);
    }

    /**
     * realShowLong
     * @param context context
     * @param message message
     */
    private static void realShowLong(Context context, CharSequence message) {
        if (context == null) return;
        if (TextUtils.isEmpty(message)) return;
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            ThreadUtils.runOnUIThread(() -> realShow(context, message, Toast.LENGTH_LONG));
        } else {
            realShow(context, message, Toast.LENGTH_LONG);
        }
    }

    @SuppressLint("ShowToast")
    private static Toast make(Context context, CharSequence str, int duration) {
        if (context != null) {
            Toast toast = Toast.makeText(context, "", duration);
            toast.setText(str);
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                hookN(toast);
            }
            if (!isNotificationEnabled(context)) {
                hook(toast);
            }
            return toast;
        }
        return null;
    }

    /**
     * 是否有通知权限
     * @param context context
     * @return true
     */
    private static boolean isNotificationEnabled(Context context) {
        return context != null && NotificationManagerCompat.from(context).areNotificationsEnabled();
    }

    /**
     * realShow
     * @param context context
     * @param message message
     */
    private static void realShow(@NonNull Context context, CharSequence message, int duration) {
        if (!TextUtils.isEmpty(message)) {
            try {
                if (toast != null) {
                    toast.cancel();
                }
                toast = make(context, message, duration);
                if (toast != null) {
                    toast.show();
                }
            } catch (Exception ex) {
                LogUtils.e("Toast show error:" + ex.getMessage());
            }
        }
    }

    private static Object iNotificationManagerObj;

    /**
     * 无通知权限hook
     * @param toast toast
     */
    private static void hook(Toast toast) {
        try {
            //hook INotificationManager
            if (iNotificationManagerObj == null) {
                Method getServiceMethod = Toast.class.getDeclaredMethod("getService");
                getServiceMethod.setAccessible(true);
                iNotificationManagerObj = getServiceMethod.invoke(null);

                Class iNotificationManagerCls = Class.forName("android.app.INotificationManager");
                Object iNotificationManagerProxy = Proxy.newProxyInstance(toast.getClass().getClassLoader(), new Class[]{iNotificationManagerCls}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //强制使用系统Toast
                        if ("enqueueToast".equals(method.getName())) {
                            args[0] = "android";
                        }
                        return method.invoke(iNotificationManagerObj, args);
                    }
                });
                Field sServiceFiled = Toast.class.getDeclaredField("sService");
                sServiceFiled.setAccessible(true);
                sServiceFiled.set(null, iNotificationManagerProxy);
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

    /**
     * android N hook
     * @param toast toast
     */
    private static void hookN(Toast toast) {
        try {
            Field fieldTN = Toast.class.getDeclaredField("mTN");
            fieldTN.setAccessible(true);
            Object mTN = fieldTN.get(toast);
            Field fieldHandler = fieldTN.getType().getDeclaredField("mHandler");
            fieldHandler.setAccessible(true);
            Handler handler = (Handler) fieldHandler.get(mTN);
            fieldHandler.set(mTN, new SafeHandler(handler));
        } catch (Exception ignored) {
        }
    }

    static final class SafeHandler extends Handler {

        private Handler mHandler;

        SafeHandler(Handler handler) {
            mHandler = handler;
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                mHandler.handleMessage(msg);
            } catch (WindowManager.BadTokenException ignored) {
            }
        }
    }
}


