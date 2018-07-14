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

package codewars.tuigroup.com.codewars.ui.widget;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

/**
 * ContentLoadingProgressBar from android.support.v4.widget.ContentLoadingProgressBar doesn't work.
 * <p>
 * mPostedShow is false, and show() sets it to true as it posts mDelayedShow.
 * When mDelayedShow is run, mPostedShow will be re-set to false.
 * However hide() is executed before mDelayShow, and it removes the mDelayedShow message
 * without resetting mPostedShow to false.
 * Any further calls to show() will not post mDelayedShow and thus never display the progress bar.
 * <p>
 * https://issuetracker.google.com/issues/37134535
 * <p>
 * ContentLoadingProgressBar implements a ProgressBar that waits a minimum time to be
 * dismissed before showing. Once visible, the progress bar will be visible for
 * a minimum amount of time to avoid "flashes" in the UI when an event could take
 * a largely variable time to complete (from none, to a user perceivable amount)
 */
public class ContentLoadingProgressBar extends ProgressBar {
    private static final long MIN_SHOW_TIME = 500L; // ms
    private static final long MIN_DELAY = 500L; // ms

    private boolean mIsAttachedToWindow = false;
    private boolean mIsShown;
    long mStartTime = -1L;

    private final Runnable mDelayedHide = new Runnable() {

        @Override
        public void run() {
            setVisibility(View.GONE);
            mStartTime = -1L;
        }
    };

    private final Runnable mDelayedShow = new Runnable() {

        @Override
        public void run() {
            mStartTime = SystemClock.uptimeMillis();
            setVisibility(View.VISIBLE);
        }
    };

    public ContentLoadingProgressBar(Context context) {
        this(context, null, 0);
    }

    public ContentLoadingProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContentLoadingProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mIsShown = getVisibility() == View.VISIBLE;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mIsAttachedToWindow = true;
        if (mIsShown && (getVisibility() != View.VISIBLE)) {
            postDelayed(mDelayedShow, MIN_DELAY);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIsAttachedToWindow = false;
        removeCallbacks(mDelayedHide);
        removeCallbacks(mDelayedShow);
        if (!mIsShown && mStartTime != -1L) {
            setVisibility(View.GONE);
        }
        mStartTime = -1L;
    }

    /**
     * Hide the progress view if it is visible. The progress view will not be
     * hidden until it has been shown for at least a minimum show time. If the
     * progress view was not yet visible, cancels showing the progress view.
     */
    public void hide() {
        if (mIsShown) {
            mIsShown = false;
            if (mIsAttachedToWindow) {
                removeCallbacks(mDelayedShow);
            }
            long diff = SystemClock.uptimeMillis() - mStartTime;
            if (mStartTime == -1L || diff >= MIN_SHOW_TIME) {
                // The progress spinner has been shown long enough
                // OR was not shown yet. If it wasn't shown yet,
                // it will just never be shown.
                setVisibility(View.GONE);
                mStartTime = -1L;
            } else {
                // The progress spinner is shown, but not long enough,
                // so put a delayed message in to hide it when its been
                // shown long enough.
                postDelayed(mDelayedHide, MIN_SHOW_TIME - diff);
            }
        }
    }

    /**
     * Show the progress view after waiting for a minimum delay. If
     * during that time, hide() is called, the view is never made visible.
     */
    public void show() {
        if (!mIsShown) {
            mIsShown = true;
            if (mIsAttachedToWindow) {
                removeCallbacks(mDelayedHide);
                if (mStartTime == -1L) {
                    postDelayed(mDelayedShow, MIN_DELAY);
                }
            }
        }
    }

    public boolean isViewShown() {
        return mIsShown;
    }
}
