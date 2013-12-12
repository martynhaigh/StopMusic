/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.martynhaigh.glass.stopmusic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import java.util.concurrent.TimeUnit;

public class ConfirmationDrawer implements SurfaceHolder.Callback {
    private static final String LOGTAG = "ConfirmationDrawer";

    private static final int COUNT_DOWN_VALUE = 1;

    private final ConfirmationView mConfirmationView;

    private SurfaceHolder mHolder;
    private Activity activity;

    public ConfirmationDrawer(Context context, int textId) {
        mConfirmationView = new ConfirmationView(context);
        mConfirmationView.setCountDown(COUNT_DOWN_VALUE);
        mConfirmationView.setTextResId(textId);
        mConfirmationView.setListener(new ConfirmationView.ConfirmationListener() {
            @Override
            public void onTick(long millisUntilFinish) {
                draw(mConfirmationView);
            }

            @Override
            public void onFinish() {
                activity.finish();
            }
        });
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Measure and layout the view with the canvas dimensions.
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

        mConfirmationView.measure(measuredWidth, measuredHeight);
        mConfirmationView.layout(
                0, 0, mConfirmationView.getMeasuredWidth(), mConfirmationView.getMeasuredHeight());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHolder = holder;
        mConfirmationView.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHolder = null;
    }

    /**
     * Draws the view in the SurfaceHolder's canvas.
     */
    private void draw(View view) {
        Canvas canvas;
        try {
            canvas = mHolder.lockCanvas();
        } catch (Exception e) {
            return;
        }
        if (canvas != null) {
            view.draw(canvas);
            mHolder.unlockCanvasAndPost(canvas);
        }
    }

}
