package com.pro.feng.codepro.util;

import android.content.Context;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.pro.feng.codepro.span.RadiusDeleteSpan;
import com.pro.feng.codepro.span.UnSpanText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Feng on 2017/12/1.
 */

public class SpanUtil {
    private static final int MAX_PHONE_COUNT = 10;
    private static final String MAX_PHONE_TIP = "最多填写" + MAX_PHONE_COUNT + "个号码";

    /* 重新生成EditText内的RadiusDeleteSpan */
    public static void rebuildSpan(EditText etInput,Context context,int radiusColor,int textColor) {
        Editable editSpanText = etInput.getText();

        List<UnSpanText> unSpanTexts = SpanUtil.getAllUnSpanTexts(editSpanText,context);
        for (UnSpanText unSpanText : unSpanTexts) {
            SpanUtil.buildRadiusDeleteSpan(editSpanText, unSpanText,radiusColor,textColor,context,etInput);
        }
        etInput.setText(editSpanText);
        etInput.setSelection(editSpanText.length());
    }


    /* 获取所有非Span的文本(普通文本) */
    public static List<UnSpanText> getAllUnSpanTexts(Editable editSpanText, Context context) {
        List<UnSpanText> unSpanTexts = new ArrayList<UnSpanText>();
        RadiusDeleteSpan[] spans = editSpanText.getSpans(0, editSpanText.length(), RadiusDeleteSpan.class);

        List<Integer> sortStartEnds = new ArrayList<Integer>();
        sortStartEnds.add(0);

        for (RadiusDeleteSpan phoneSpan : spans) {
            sortStartEnds.add(editSpanText.getSpanStart(phoneSpan));
            sortStartEnds.add(editSpanText.getSpanEnd(phoneSpan));
        }
        sortStartEnds.add(editSpanText.length());
        Collections.sort(sortStartEnds);

        // 最多三个号码，记录超出三个的Span，将超出的文本删除
        List<UnSpanText> overSizeTexts = new ArrayList<UnSpanText>();

        for (int i = 0; i < sortStartEnds.size(); i = i + 2) {
            int start = sortStartEnds.get(i);
            int end = sortStartEnds.get(i + 1);
            CharSequence text = editSpanText.subSequence(start, end);
            if (!TextUtils.isEmpty(text)) {
                if (unSpanTexts.size() >= MAX_PHONE_COUNT - spans.length) {
                    overSizeTexts.add(new UnSpanText(start, end, text.toString()));
                    continue;
                }
                unSpanTexts.add(new UnSpanText(start, end, text.toString()));
            }
        }

        // 将超出的文本删除
        if (overSizeTexts.size() > 0) {
            for (int i = overSizeTexts.size() - 1; i >= 0; i--) {
                UnSpanText overText = overSizeTexts.get(i);
                editSpanText.delete(overText.start, overText.end);
            }
            Toast.makeText(context, MAX_PHONE_TIP, Toast.LENGTH_SHORT).show();
        }

        return unSpanTexts;
    }

    /* 构建一个Span */
    public static void buildRadiusDeleteSpan(Editable editSpanText, UnSpanText unSpanText, int radiusColor, int textColor, Context context,EditText etInput) {
        RadiusDeleteSpan span = new RadiusDeleteSpan(radiusColor, textColor, 5, context, unSpanText.showText, new RadiusDeleteSpan.OnRadiusClickLister() {
            @Override
            public void onClickSpan(RadiusDeleteSpan span, String spanText) {
                // 删除点击的文本内容
                etInput.getText().replace(etInput.getText().getSpanStart(span), etInput.getText().getSpanEnd(span), "");
                // 删除Span
                etInput.getText().removeSpan(span);
            }
        });

        editSpanText.setSpan(span, unSpanText.start, unSpanText.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
