/*
 * Copyright 2017 olivermsw
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pers.olimsw.rfllibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class RoundFrameLayout extends FrameLayout {
    private float[] mBorders = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
    private float mBorderWidth;
    private int mBorderColor;
    private float mShadowRadius;
    private float mShadowDx;
    private float mShadowDy;
    private int mShadowColor;
    private boolean mRoundAsCircle;
    private Path mPath;
    private Paint mPaint;
    private RectF drawArea;

    public RoundFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public RoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundFrameLayout);
        float mBorderRadius = typedArray.getDimension(R.styleable.RoundFrameLayout_border_radius, 0);
        float mBorderTopLeft = typedArray.getDimension(R.styleable.RoundFrameLayout_border_radius_top_left, mBorderRadius);
        float mBorderTopRight = typedArray.getDimension(R.styleable.RoundFrameLayout_border_radius_top_right, mBorderRadius);
        float mBorderBottomLeft = typedArray.getDimension(R.styleable.RoundFrameLayout_border_radius_bottom_left, mBorderRadius);
        float mBorderBottomRight = typedArray.getDimension(R.styleable.RoundFrameLayout_border_radius_bottom_right, mBorderRadius);
        mBorderWidth = typedArray.getDimension(R.styleable.RoundFrameLayout_border_width, 0);
        mBorderColor = typedArray.getColor(R.styleable.RoundFrameLayout_border_color, Color.WHITE);
        mRoundAsCircle = typedArray.getBoolean(R.styleable.RoundFrameLayout_round_as_circle, false);
        mShadowColor = typedArray.getColor(R.styleable.RoundFrameLayout_shadow_color, Color.GRAY);
        mShadowRadius = typedArray.getDimension(R.styleable.RoundFrameLayout_shadow_radius, 0);
        mShadowDx = typedArray.getDimension(R.styleable.RoundFrameLayout_shadow_dx, 0);
        mShadowDy = typedArray.getDimension(R.styleable.RoundFrameLayout_shadow_dy, 0);
        typedArray.recycle();
        mBorders[0] = mBorderTopLeft;
        mBorders[1] = mBorderTopLeft;
        mBorders[2] = mBorderTopRight;
        mBorders[3] = mBorderTopRight;
        mBorders[4] = mBorderBottomRight;
        mBorders[5] = mBorderBottomRight;
        mBorders[6] = mBorderBottomLeft;
        mBorders[7] = mBorderBottomLeft;
        mPath = new Path();
        mPaint = new Paint();
        drawArea = new RectF();
        mPaint.setAntiAlias(true);
        float absDy = Math.abs(mShadowDy);
        float absDx = Math.abs(mShadowDx);
        int shadow = (int) (Math.min(mShadowRadius, Math.max(absDy, absDx)) + mShadowRadius);
        if (shadow > 0) {
            setPadding(getPaddingLeft() + shadow, getPaddingTop() + shadow, getPaddingRight() + shadow, getPaddingBottom() + shadow);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initData();
    }

    private void initData() {
        drawArea.left = getPaddingLeft();
        drawArea.top = getPaddingTop();
        drawArea.right = getWidth() - getPaddingRight();
        drawArea.bottom = getHeight() - getPaddingBottom();
        if (mRoundAsCircle) {
            float r = drawArea.width() > drawArea.height() ? drawArea.height() / 2 : drawArea.width() / 2;
            mPath.addCircle(drawArea.centerX(), drawArea.centerY(), r, Path.Direction.CCW);
        } else {
            mPath.addRoundRect(drawArea, mBorders, Path.Direction.CCW);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void dispatchDraw(Canvas canvas) {
        int layerID = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        mPaint.setColor(mShadowColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mShadowColor);
        canvas.drawPath(mPath, mPaint);
        canvas.restoreToCount(layerID);
        layerID = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.clipPath(mPath);
        super.dispatchDraw(canvas);
        mPaint.setColor(mBorderColor);
        mPaint.setShadowLayer(0, mShadowDx, mShadowDy, mShadowColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth * 2);
        canvas.drawPath(mPath, mPaint);
        canvas.restoreToCount(layerID);
    }


}
