package com.chends.note;

import android.content.Context;
import android.content.Intent;

import com.chends.note.base.Constant;
import com.chends.note.main.MainActivity;

/**
 * @author cds created on 2019/8/4.
 */
public class AppPageDispatch {

    /**
     * buildIntent
     * @param context context
     * @param cls     cls
     * @return Intent
     */
    private static Intent buildIntent(Context context, Class<?> cls) {
        return new Intent(context, cls);
    }

    /**
     * 打开主页
     * @param context context
     */
    public static void startMainPageRecreate(Context context) {
        Intent intent = buildIntent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constant.BOOLEAN_PARAM, true);
        context.startActivity(intent);
    }
}
