package com.chends.note.model;

import java.util.Locale;

/**
 * 语言
 * @author cds created on 2019/8/4.
 */
public class LanguageBean {
    public String key; // key
    public String name; // 显示名称
    public Locale locale; // 语言

    public LanguageBean(String key, String name, Locale locale) {
        this.key = key;
        this.name = name;
        this.locale = locale;
    }
}
