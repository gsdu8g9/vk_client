package github.ankushsachdeva.emojicon;

import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 16.10.15.
 */
public class RepeatListener implements View.OnTouchListener {

    private final int normalInterval;
    private final View.OnClickListener clickListener;
    private Handler handler = new Handler();
    private int initialInterval;
    private View downView;
    private Runnable handlerRunnable = new Runnable() {
        @Override
        public void run() {
            if (downView == null) {
                return;
            }
            handler.removeCallbacksAndMessages(downView);
            handler.postAtTime(this, downView, SystemClock.uptimeMillis() + normalInterval);
            clickListener.onClick(downView);
        }
    };

    /**
     * @param initialInterval The interval before first click event
     * @param normalInterval The interval before second and subsequent click
     * events
     * @param clickListener The OnClickListener, that will be called
     * periodically
     */
    public RepeatListener(int initialInterval,
            int normalInterval,
            View.OnClickListener clickListener) {
        if (clickListener == null) {
            throw new IllegalArgumentException("null runnable");
        }

        if (initialInterval < 0 || normalInterval < 0) {
            throw new IllegalArgumentException("negative interval");
        }

        this.initialInterval = initialInterval;
        this.normalInterval = normalInterval;
        this.clickListener = clickListener;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downView = view;
                handler.removeCallbacks(handlerRunnable);
                handler.postAtTime(handlerRunnable, downView,
                        SystemClock.uptimeMillis() + initialInterval);
                clickListener.onClick(view);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                handler.removeCallbacksAndMessages(downView);
                downView = null;
                return true;
        }
        return false;
    }
}