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
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.Long;
import java.util.concurrent.TimeUnit;

public class ConfirmationView extends FrameLayout {
    private static final String LOGTAG = "ConfirmationView";

    /**
     * Interface to listen for changes in the confirmation.
     */
    public interface ConfirmationListener {
        /**
         * Notified of a tick, indicating a layout change.
         */
        public void onTick(long millisUntilFinish);

        /**
         * Notified when the confirmation is finished.
         */
        public void onFinish();
    }

    /** Time delimiter specifying when the second component is fully shown. */
    public static final float ANIMATION_DURATION_IN_MILLIS = 850.0f;

    // About 24 FPS.
    private static final long DELAY_MILLIS = 41;
    private static final int MAX_TRANSLATION_Y = 30;
    private static final float ALPHA_DELIMITER = 0.95f;
    private static final long SEC_TO_MILLIS = TimeUnit.SECONDS.toMillis(1);

    private final TextView mConfirmationView;

    private long mTimeSeconds;
    private long mStopTimeInFuture;
    private ConfirmationListener mListener;
    private boolean mStarted;

    public ConfirmationView(Context context) {
        this(context, null, 0);
    }

    public ConfirmationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConfirmationView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        LayoutInflater.from(context).inflate(R.layout.card_confirmation, this);

        mConfirmationView =  (TextView) findViewById(R.id.confirmation_text);
    }

    public void setTextResId(int textId) {
        mConfirmationView.setText(textId);
    }

    public void setCountDown(long timeSeconds) {
        mTimeSeconds = timeSeconds;
    }

    /**
     * Set a {@link ConfirmationView.ConfirmationListener}.
     */
    public void setListener(ConfirmationListener listener) {
        mListener = listener;
    }

    private final Handler mHandler = new Handler();

    private final Runnable mUpdateViewRunnable = new Runnable() {
        @Override
        public void run() {
            final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

            // Count down is done.
            if (millisLeft <= 0) {
                mStarted = false;
                if (mListener != null) {
                    mListener.onFinish();
                }
            } else {
                updateView(millisLeft);
                if (mListener != null) {
                    mListener.onTick(millisLeft);
                }
                mHandler.postDelayed(mUpdateViewRunnable, DELAY_MILLIS);
            }
        }
    };

    /**
     * Starts the countdown animation if not yet started.
     */
    public void start() {
        if (!mStarted) {
            mStopTimeInFuture =
                    TimeUnit.SECONDS.toMillis(mTimeSeconds) + SystemClock.elapsedRealtime();
            mStarted = true;
            mHandler.postDelayed(mUpdateViewRunnable, DELAY_MILLIS);
        }
    }

    /**
     * Updates the views to reflect the current state of animation.
     *
     * @params millisUntilFinish milliseconds until the countdown is done
     */
    private void updateView(long millisUntilFinish) {

        long frame = SEC_TO_MILLIS - (millisUntilFinish % SEC_TO_MILLIS);

        if (frame <= ANIMATION_DURATION_IN_MILLIS) {
            float factor = frame / ANIMATION_DURATION_IN_MILLIS;
            mConfirmationView.setAlpha(factor * ALPHA_DELIMITER);
            mConfirmationView.setTranslationY(MAX_TRANSLATION_Y * (1 - factor));
        } else {
            float factor = (frame - ANIMATION_DURATION_IN_MILLIS) / ANIMATION_DURATION_IN_MILLIS;
            mConfirmationView.setAlpha(ALPHA_DELIMITER + factor * (1 - ALPHA_DELIMITER));
        }
    }


}
