package com.pro.feng.codepro.span;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.text.style.ReplacementSpan;
import android.view.View;

import com.pro.feng.codepro.R;
import com.pro.feng.codepro.util.DisplayUtil;


/**
 * Created by Feng on 2017/3/8.
 */

public class RadiusDeleteSpan extends ReplacementSpan {

    private int mSize;
    private int mColor;
    private int mRadius;
    private int tColor;
    private Context mContext;
    private OnRadiusClickLister mListener;
    private String spanContent;
    private RectF ovalRect; // Span的内容矩阵

    /**
     * @param bgColor 背景颜色
     * @param radius  圆角半径
     */
    public RadiusDeleteSpan(int bgColor, int textColor, int radius, Context context, String spanContent, OnRadiusClickLister lister) {
        mColor = bgColor;
        mRadius = radius;
        tColor = textColor;
        this.mContext = context;
        this.spanContent = spanContent;
        this.mListener = lister;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        mSize = (int) (paint.measureText(text, start, end) + 2 * mRadius) + 20;
        //mSize就是span的宽度，span有多宽，开发者可以在这里随便定义规则
        //我的规则：这里text传入的是SpannableString，start，end对应setSpan方法相关参数
        //可以根据传入起始截至位置获得截取文字的宽度，最后加上左右两个圆角的半径得到span宽度
        return mSize + DisplayUtil.dip2px(mContext, 10);//让每个span 之间空10dp
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        //int color = paint.getColor();//保存文字颜色
        paint.setColor(mColor);//设置背景颜色
        paint.setAntiAlias(true);// 设置画笔的锯齿效果

        BitmapDrawable bd = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.icon_delete_span);
        Bitmap mBitMap = bd.getBitmap();
        ovalRect = new RectF(x, y + paint.ascent() - 5, x + mSize + mBitMap.getWidth(), y + paint.descent() + 5);
        //设置文字背景矩形，x为span其实左上角相对整个TextView的x值，y为span左上角相对整个View的y值。paint.ascent()获得文字上边缘，paint.descent()获得文字下边缘
        canvas.drawRoundRect(ovalRect, mRadius, mRadius, paint);//绘制圆角矩形，第二个参数是x半径，第三个参数是y半径
        canvas.drawBitmap(mBitMap, x + mSize - 10, y + (paint.descent() + paint.ascent()) / 2-mBitMap.getHeight()/2, paint);
        paint.setColor(tColor);//恢复画笔的文字颜色
        canvas.drawText(text, start, end, x + mRadius + 10, y, paint);//绘制文字
        mBitMap = null;
    }

    public void onClick(View v) {
        if (mListener != null) {
            mListener.onClickSpan(this, spanContent);
        }
    }

    public interface OnRadiusClickLister {
        void onClickSpan(RadiusDeleteSpan span, String spanText);
    }

    public String getSpanContent() {
        return spanContent;
    }

    public void setSpanContent(String spanContent) {
        this.spanContent = spanContent;
    }

    public RectF getOvalRect() {
        return ovalRect;
    }
}
