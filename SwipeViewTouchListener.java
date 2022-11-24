import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwipeViewTouchListener implements View.OnTouchListener {

    // Cached ViewConfiguration and system-wide constant values
    private final int mSlop;
    private final int mMinFlingVelocity;
    private final int mMaxFlingVelocity;
    private final long mAnimationTime;

    // Fixed properties
    private final View mView;
    private final OnSwipeCallback mCallback;
    private int mViewWidth = 1;
    private boolean dismissLeft = true;
    private boolean dismissRight = true;

    // Transient properties
    private final List<PendingSwipeData> mPendingSwipes = new ArrayList<>();
    private int mDismissAnimationRefCount = 0;
    private float mDownX;
    private boolean mSwiping;
    private VelocityTracker mVelocityTracker;
    private int mDownPosition;
    private View mDownView;
    private boolean mPaused;

    /**
     * The callback interface used by {@link SwipeViewTouchListener} to inform its client
     * about a successful swipe of view.
     */
    public interface OnSwipeCallback {
        /**
         * Called when the user has swiped the view item to the left.
         *
         * @param view               The originating {@link View}.
         */
        void onSwipeLeft(View view);

        void onSwipeRight(View view);
    }

    /**
     * Constructs a new swipe-to-action touch listener for the given view.
     *
     * @param view The view who should be dismissible.
     * @param callback The callback to trigger when the user has indicated that she would like to
     *                 dismiss the view.
     */
    public SwipeViewTouchListener(View view, OnSwipeCallback callback) {
        ViewConfiguration vc = ViewConfiguration.get(view.getContext());
        mSlop = vc.getScaledTouchSlop();
        mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        mAnimationTime = view.getContext().getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        mView = view;
        mCallback = callback;
    }

    /**
     * Constructs a new swipe-to-action touch listener for the given view.
     *
     * @param callback The callback to trigger when the user has indicated that he would like to
     *                 dismiss one view.
     * @param dismissLeft set if the dismiss animation is up when the user swipe to the left
     * @param dismissRight set if the dismiss animation is up when the user swipe to the right
     */
    public SwipeViewTouchListener(View view, OnSwipeCallback callback, boolean dismissLeft, boolean dismissRight) {
        this(view, callback);
        this.dismissLeft = dismissLeft;
        this.dismissRight = dismissRight;
    }

    /**
     * Enables or disables (pauses or resumes) watching for swipe-to-dismiss gestures.
     *
     * @param enabled Whether or not to watch for gestures.
     */
    public void setEnabled(boolean enabled) {
        mPaused = !enabled;
    }



    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (mViewWidth < 2) {
            //To make the view slide-able using parent width use this:
            //mViewWidth = ((View)(view.getParent())).getWidth();
            //To make the view slide-able using its own width use this:
            mViewWidth = mView.getWidth();
        }

        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            {
                if (mPaused) {
                    return false;
                }

                mDownView = view;
                mDownX = motionEvent.getRawX();
                mDownPosition = view.getBottom();
                mVelocityTracker = VelocityTracker.obtain();
                mVelocityTracker.addMovement(motionEvent);

                view.onTouchEvent(motionEvent);
                return true;
            }

            case MotionEvent.ACTION_UP:
            {
                if (mVelocityTracker == null) {
                    break;
                }

                float deltaX = motionEvent.getRawX() - mDownX;
                mVelocityTracker.addMovement(motionEvent);
                mVelocityTracker.computeCurrentVelocity(500);
                float velocityX = Math.abs(mVelocityTracker.getXVelocity());
                float velocityY = Math.abs(mVelocityTracker.getYVelocity());
                boolean swipe = false;
                boolean swipeRight = false;

                if (Math.abs(deltaX) > (float)(mViewWidth / 2)) {
                    swipe = true;
                    swipeRight = deltaX > 0;
                } else if (mMinFlingVelocity <= velocityX && velocityX <= mMaxFlingVelocity && velocityY < velocityX) {
                    swipe = true;
                    swipeRight = mVelocityTracker.getXVelocity() > 0;
                }
                if (swipe) {
                    // sufficient swipe value
                    final View downView = mDownView;
                    final int downPosition = mDownPosition;
                    final boolean toTheRight = swipeRight;
                    ++mDismissAnimationRefCount;
                    mDownView.animate()
                            .translationX(swipeRight ? mViewWidth : -mViewWidth)
                            .alpha(0)
                            .setDuration(mAnimationTime)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    performSwipeAction(downView, downPosition, toTheRight, toTheRight ? dismissRight : dismissLeft);
                                }
                            });
                } else {
                    // cancel
                    mDownView.animate()
                            .translationX(0)
                            .alpha(1)
                            .setDuration(mAnimationTime)
                            .setListener(null);
                }
                mVelocityTracker = null;
                mDownX = 0;
                mDownView = null;
                mDownPosition = -1;
                mSwiping = false;
                break;
            }

            case MotionEvent.ACTION_MOVE:
            {
                if (mVelocityTracker == null || mPaused) {
                    break;
                }

                mVelocityTracker.addMovement(motionEvent);
                float deltaX = motionEvent.getRawX() - mDownX;
                if (Math.abs(deltaX) > mSlop) {
                    mSwiping = true;

                    MotionEvent cancelEvent = MotionEvent.obtain(motionEvent);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL |
                            (motionEvent.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                    mView.onTouchEvent(cancelEvent);
                }

                if (mSwiping) {
                    mDownView.setTranslationX(deltaX);
                    mDownView.setAlpha(Math.max(0f, Math.min(1f,
                            1f - 2f * Math.abs(deltaX) / mViewWidth)));
                    return true;
                }
                break;
            }
        }
        return false;
    }

    static class PendingSwipeData implements Comparable <PendingSwipeData> {
        public int position;
        public View view;

        public PendingSwipeData(int position, View view) {
            this.position = position;
            this.view = view;
        }

        @Override
        public int compareTo(PendingSwipeData other) {
            // Sort by descending position
            return other.position - position;
        }
    }

    private void performSwipeAction(final View swipeView, final int swipePosition, boolean toTheRight, boolean dismiss) {
        // Animate the dismissed list item to zero-height and fire the dismiss callback when
        // all dismissed list item animations have completed. This triggers layout on each animation
        // frame; in the future we may want to do something smarter and more performant.
        final ViewGroup.LayoutParams lp = swipeView.getLayoutParams();
        final int originalHeight = swipeView.getHeight();
        final boolean swipeRight = toTheRight;

        ValueAnimator animator;
        if (dismiss) {
            animator = ValueAnimator.ofInt(originalHeight, 1).setDuration(mAnimationTime);
        }
        else {
            animator = ValueAnimator.ofInt(originalHeight, originalHeight - 1).setDuration(mAnimationTime);
        }


        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                --mDismissAnimationRefCount;
                if (mDismissAnimationRefCount == 0) {
                    // No active animations, process all pending dismisses.
                    // Sort by descending position
                    Collections.sort(mPendingSwipes);

                    if (swipeRight) {
                        mCallback.onSwipeRight(mView);
                    }
                    else {
                        mCallback.onSwipeLeft(mView);
                    }


                    for (PendingSwipeData pendingDismiss: mPendingSwipes) {
                        // Reset view presentation
                        ViewGroup.LayoutParams lp;
                        if (pendingDismiss.view != null) {
                            pendingDismiss.view.setAlpha(1f);
                            pendingDismiss.view.setTranslationX(0);
                            lp = pendingDismiss.view.getLayoutParams();
                            lp.height = originalHeight;
                            pendingDismiss.view.setLayoutParams(lp);
                        }

                    }
                    mPendingSwipes.clear();
                }
            }
        });

        animator.addUpdateListener(valueAnimator -> {
            lp.height = (Integer) valueAnimator.getAnimatedValue();
            swipeView.setLayoutParams(lp);
        });

        mPendingSwipes.add(new PendingSwipeData(swipePosition, swipeView));
        animator.start();
    }
}
