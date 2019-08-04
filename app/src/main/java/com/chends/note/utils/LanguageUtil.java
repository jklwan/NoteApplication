package com.chends.note.utils;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.chends.note.R;
import com.chends.note.model.LanguageBean;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * 语言切换
 * @author chends create on 2019/3/20.
 */
public class LanguageUtil {

    private final String SpName = "language";
    private final String SpKey = "languageSelect";
    private final String AUTO = "auto";

    private final Map<String, LanguageBean> map = new LinkedHashMap<String, LanguageBean>() {
        {
            put(AUTO, new LanguageBean(AUTO, "跟随系统", Locale.getDefault()));
            put("cn", new LanguageBean("cn", "简体中文", Locale.SIMPLIFIED_CHINESE));
            put("tw", new LanguageBean("tw", "繁體中文（台灣）", Locale.TRADITIONAL_CHINESE));
            put("en", new LanguageBean("en", "English", Locale.ENGLISH));
        }
    };

    private static volatile LanguageUtil util;
    private String LanguageSelect;
    private Application app;

    private LanguageUtil(Application app) {
        this.app = app;
        LanguageSelect = getSpSaveKey(app.getApplicationContext());
    }

    private void setAuto() {
        map.put(AUTO, new LanguageBean(AUTO, app.getApplicationContext().getResources().getString(R.string.auto), getSystemLocale()));
    }

    /**
     * init
     */
    private void initInner() {
        if (TextUtils.isEmpty(LanguageSelect)) {
            LanguageSelect = getSpSaveKey(app.getApplicationContext());
        }
        innerConfigurationChanged();
    }

    public static void init(Application app) {
        if (util == null) {
            synchronized (LanguageUtil.class) {
                if (util == null) {
                    util = new LanguageUtil(app);
                }
            }
        }
        util.initInner();
    }

    /**
     * select
     * @return select
     */
    private String getSelectInner() {
        if (TextUtils.isEmpty(LanguageSelect)) {
            LanguageSelect = getSpSaveKey(app.getApplicationContext());
        }
        return LanguageSelect;
    }

    /**
     * select
     * @return select
     */
    public static String getLanguageSelect() {
        return util.getSelectInner();
    }

    /**
     * 获取list
     * @return list
     */
    private List<LanguageBean> getLanguageInner() {
        return new ArrayList<>(map.values());
    }

    /**
     * 获取列表
     * @return language list
     */
    public static List<LanguageBean> getLanguage() {
        return util.getLanguageInner();
    }

    /**
     * attachBase
     * @param base base
     * @return Context
     */
    public static Context attachBaseContext(Context base) {
        return util.attachBaseContextInner(base);
    }

    /**
     * attachBase
     * @param base base
     * @return Context
     */
    private Context attachBaseContextInner(Context base) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(base);
        } else {
            setConfiguration(base);
            return base;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Context updateResources(Context context) {
        Resources resources = context.getResources();
        Locale locale = getLocale();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    /**
     * 设置语言
     */
    private void setConfiguration(Context context) {
        Resources resources = context.getResources();
        Locale targetLocale = getLocale();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = targetLocale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());//语言更换生效的代码!
    }

    /**
     * 当前配置的语言
     * @return Locale
     */
    private Locale getLocale() {
        LanguageBean bean = map.get(LanguageSelect);
        if (bean != null) {
            return bean.locale;
        }
        return Locale.CHINESE;
    }


    /**
     * 已保存的key
     * @param context context
     * @return key
     */
    @NonNull
    private String getSpSaveKey(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SpName, Context.MODE_PRIVATE);
        String key = sp.getString(SpKey, "");
        if (TextUtils.isEmpty(key)) {
            saveKey(context, AUTO);
            key = AUTO;
        }
        return key;
    }

    /**
     * 保存
     * @param context context
     * @param key     language key
     */
    private boolean saveKey(Context context, String key) {
        LanguageSelect = key;
        SharedPreferences spLocal = context.getSharedPreferences(SpName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spLocal.edit();
        return edit.putString(SpKey, key).commit();
    }

    /**
     * 更新
     * @param context context
     * @param bean    LanguageBean
     * @return true false
     */
    public static boolean updateLanguage(Context context, LanguageBean bean) {
        return util.updateLanguageInner(context, bean);
    }

    /**
     * 更新
     * @param context context
     * @param bean    LanguageBean
     * @return true false
     */
    private boolean updateLanguageInner(Context context, LanguageBean bean) {
        if (TextUtils.equals(LanguageSelect, bean.key)) {
            // 无需任何变化
            return false;
        }
        if (saveKey(context, bean.key)) {
            innerConfigurationChanged();
        }
        return true;
    }

    /**
     * 当前系统语言
     * @return true
     */
    private static Locale getSystemLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //7.0有多语言设置获取顶部的语言
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            locale = Resources.getSystem().getConfiguration().locale;
        }
        return locale;
    }

    private boolean innerConfigurationChanged() {
        setAuto();
        setConfiguration(app.getApplicationContext());
        Objects.requireNonNull(map.get(AUTO)).name = app.getApplicationContext().getResources().getString(R.string.auto);
        return TextUtils.equals(getSelectInner(), AUTO);
    }

    public static boolean onConfigurationChanged() {
        return util.innerConfigurationChanged();
    }
}