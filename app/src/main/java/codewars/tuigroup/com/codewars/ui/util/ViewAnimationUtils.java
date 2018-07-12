package codewars.tuigroup.com.codewars.ui.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

public class ViewAnimationUtils {

    /**
     * Sets the visibility of the specified view to View.VISIBLE and then fades it in. If the
     * view is already visible, the method will return without doing anything.
     *
     * @param view The view to be faded in
     */
    public static void fadeInView(View view) {
        if (view.getVisibility() == View.INVISIBLE || view.getVisibility() == View.GONE) {
            int shortAnimationDuration = view.getResources().getInteger(android.R.integer.config_shortAnimTime);
            view.setAlpha(0f);
            view.setVisibility(View.VISIBLE);

            view.animate()
                    .alpha(1f)
                    .setDuration(shortAnimationDuration)
                    .setListener(null);
        }
    }

    /**
     * Fades out the specified view and then sets its visibility to the specified
     * value (either View.INVISIBLE or View.GONE). If the view is not currently visible, the
     * method will return without doing anything.
     *
     * @param view       The view to be hidden
     * @param visibility The value to which the view's visibility will be set after it fades out.
     *                   Must be either View.VISIBLE or View.INVISIBLE.
     */
    public static void fadeOutView(final View view, final int visibility) {
        if (view.getVisibility() == View.VISIBLE &&
                (visibility == View.INVISIBLE || visibility == View.GONE)) {
            int shortAnimationDuration = view.getResources().getInteger(android.R.integer.config_shortAnimTime);
            view.animate()
                    .alpha(0f)
                    .setDuration(shortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            view.setAlpha(1);
                            view.setVisibility(visibility);
                            view.animate().setListener(null);
                        }
                    });
        }
    }
}
