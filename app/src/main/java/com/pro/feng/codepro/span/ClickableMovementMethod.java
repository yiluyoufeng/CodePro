package com.pro.feng.codepro.span;

import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * 号码Span点击事件处理：
 * 1.防止点击计算不准确（int off = layout.getOffsetForHorizontal(line, x); 得到的是离点击最近的Span,所以会出现点击不准确）
 */
public class ClickableMovementMethod extends LinkMovementMethod {

    private static ClickableMovementMethod sInstance;

    public static ClickableMovementMethod getInstance() {
        if (sInstance == null) {
            sInstance = new ClickableMovementMethod();
        }
        return sInstance;
    }

    public boolean onTouchEvent(TextView widget, Spannable buffer,
                                MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {

            // 处理移动光标时异常
            //            java.lang.IllegalArgumentException: Invalid offset: -1. Valid range is [0, 20]
            //            at android.text.method.WordIterator.checkOffsetIsValid(WordIterator.java:380)
            int startSelection = widget.getSelectionStart();
            int endSelection = widget.getSelectionEnd();
            if (startSelection != endSelection) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    final CharSequence text = widget.getText();
                    widget.setText(null);
                    widget.setText(text);
                }
            }

            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);

            RadiusDeleteSpan[] delSpans = buffer.getSpans(0, buffer.length(), RadiusDeleteSpan.class);
            if (delSpans != null && delSpans.length > 0 && action == MotionEvent.ACTION_UP) {
                for (int i = 0; i < delSpans.length; i++) {
                    // 判断点击的x,y是否在某个span的矩阵内
                    if (delSpans[i].getOvalRect().contains(x, y)) {
                        delSpans[i].onClick(widget);
                        return true;
                    }
                }
            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }
}
