package com.chends.note.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Objects;

/**
 * @author chends create on 2019/7/23.
 */
public class IntentUtil {

    public static void startActivity(Context context, Class cls) {
        startActivity(context, cls, null);
    }

    public static void startActivity(Context context, Class cls, Bundle bundle) {
        Objects.requireNonNull(context);
        Intent intent = new Intent(context, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }
}
