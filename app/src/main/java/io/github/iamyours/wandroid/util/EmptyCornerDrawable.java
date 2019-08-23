package io.github.iamyours.wandroid.util;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import static android.graphics.PixelFormat.OPAQUE;

public class EmptyCornerDrawable extends Drawable {
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float radius = 0;

    public EmptyCornerDrawable(int color, float radius) {
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        this.radius = radius;
    }

    @Override
    public void draw(Canvas canvas) {
        RectF rectF = new RectF(getBounds().left, getBounds().top, getBounds().right, getBounds().bottom);
        canvas.drawRoundRect(rectF, radius, radius, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return OPAQUE;
    }
}
