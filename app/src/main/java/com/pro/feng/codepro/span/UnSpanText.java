package com.pro.feng.codepro.span;

import android.text.TextUtils;

/**
 * Created by Feng on 2017/3/8.
 * 表示一段非span字符串
 */
public class UnSpanText {
    public int start;
    public int end;
    public String showText;

    public UnSpanText(int start, int end, String showText) {
        if (TextUtils.isEmpty(showText))
            return;

        this.start = start;
        this.end = end;
        this.showText = showText;
    }
}
