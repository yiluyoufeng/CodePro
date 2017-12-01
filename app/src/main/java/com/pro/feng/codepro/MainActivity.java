package com.pro.feng.codepro;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.pro.feng.codepro.span.ClickableMovementMethod;
import com.pro.feng.codepro.span.RadiusDeleteSpan;
import com.pro.feng.codepro.util.SpanUtil;

public class MainActivity extends AppCompatActivity {
    private EditText etInput;
    private static final int MAX_PHONE_COUNT = 10;
    private static final String MAX_PHONE_TIP = "最多填写" + MAX_PHONE_COUNT + "个号码";

    int radiusColor;// 圆角背景颜色
    int textColor;// Span字体颜色

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etInput = findViewById(R.id.et_input);
        radiusColor = getResources().getColor(R.color.color_field_phone_bg);
        textColor = getResources().getColor(R.color.color_add_content);
        init();
    }

    private void init() {
        etInput.setMovementMethod(ClickableMovementMethod.getInstance());
        etInput.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明
        etInput.setLineSpacing(0, 1.5f);

        // 按下键盘回车时，内容可变化为Span
        etInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                SpanUtil.rebuildSpan(etInput,MainActivity.this,radiusColor,textColor);
                return true;
            }
            return false;
        });

        // 只可输入 0-9 、 + 、 - 三个字符，已有三个号码时，不可再输入内容
        etInput.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                boolean isOk = true;       // 如果含有非 0-9 、+、 - 的字符，则不使用内容
                boolean isHasSplit = false;// 如果含有 逗号 空格，则将内容生成 Span

                for (int i = start; i < end; i++) {
                    char inputChar = source.charAt(i);
                    if (('0' <= inputChar && inputChar <= '9') || inputChar == '+' || inputChar == '-') {

                    } else if (',' == inputChar || '，' == inputChar || ' ' == inputChar) {
                        isHasSplit = true;
                    } else {
//                        isOk = false;
                        break;
                    }
                }

                if (isHasSplit) {
                    source = source.toString().replaceAll(",", "").replaceAll("，", "").replaceAll(" ", "");
                    // 如果输入的 , ，空格，则将已有的号码生成Span，end=1 是区分复制和输入
                    if (end == 1) {
                        SpanUtil.rebuildSpan(etInput,MainActivity.this,radiusColor,textColor);
                    }
                }
                if (isOk) {
                    RadiusDeleteSpan[] spans = etInput.getText().getSpans(0, etInput.getText().length(), RadiusDeleteSpan.class);
                    // 如果已经输入了三个号码,则输入不生效
                    if (end == 1 && spans.length >= MAX_PHONE_COUNT) {
                        Toast.makeText(MainActivity.this, MAX_PHONE_TIP, Toast.LENGTH_SHORT).show();
                        return "";
                    }
                    return source;
                }

                return "";
            }
        }});
    }

}
