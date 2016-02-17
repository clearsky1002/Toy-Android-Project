package kristoffer.com.pointwisetoy;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import kristoffer.com.pointwisetoy.CustomView.DrawingView;

/**
 * A placeholder fragment containing a simple view.
 */
public class AnimatonActivityFragment extends Fragment {
    private ImageView           mTargetView;            // Target Star view being animated.
    private ImageView           mTargetShadowView;      // Target Start shadow view being animated and get closer to target star.
    private DrawingView         mView;                  // View for draw path
    private Paint               mPaint;                 // Path Color Paint
    private RelativeLayout      mAnimationLayout;       // Layout for animation

    public AnimatonActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_animaton, container, false);

        mTargetView = (ImageView)fragmentView.findViewById(R.id.targetView);
        mTargetShadowView = (ImageView)fragmentView.findViewById(R.id.shadowView);
        mAnimationLayout = (RelativeLayout) fragmentView.findViewById(R.id.layout_animation);

        LinearLayout layout = (LinearLayout) fragmentView.findViewById(R.id.layout_drawing);
        initPaint();

        mView = new DrawingView(this.getActivity(), mPaint);
        layout.addView(mView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        return fragmentView;
    }

    /*
     * Init Path Draw Paint such as color and width
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(3);
    }

    /*
     * Start Star Animation
     */
    public void startAnimation() {
        final Path animationPath = mView.getPath();

        if(animationPath == null)
        {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Point Wise")
                    .setMessage("Please draw path to animate, first.")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
        else
        {
            Log.d("ANIM", "Start Animation Called");
            final int targetSize = mTargetView.getLayoutParams().width;
            final int defaultShadowFar = 30;
            ValueAnimator pathAnimator = ObjectAnimator.ofFloat(0.0f, 1.0f);
            pathAnimator.setDuration(5000);

            pathAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                float[] point = new float[2];

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    float val = animation.getAnimatedFraction() / 2;
                    PathMeasure pathMeasure = new PathMeasure(animationPath, true);
                    pathMeasure.getPosTan(pathMeasure.getLength() * val, point, null);

                    Log.d("ANIM", String.format("Val: %f, XPos:%f, YPos:%f", val, point[0], point[1]));

                    int newSize = (int) ((float) targetSize * (1.0f - val * 2));
                    Log.d("ANIM", String.format("NEW SIZE: %d", newSize));

                    float newX = point[0] - mTargetView.getLayoutParams().width;
                    float newY = point[1] - mTargetView.getLayoutParams().height;
                    mTargetView.setX(newX);
                    mTargetView.setY(newY);

                    mTargetShadowView.setX(newX + defaultShadowFar * (1.0f - val * 2));
                    mTargetShadowView.setY(newY + defaultShadowFar * (1.0f - val * 2));

                    mTargetView.getLayoutParams().width = newSize;
                    mTargetView.getLayoutParams().height = newSize;
                    mTargetView.requestLayout();

                    mTargetShadowView.getLayoutParams().width = newSize;
                    mTargetShadowView.getLayoutParams().height = newSize;
                    mTargetShadowView.requestLayout();

                    if (mAnimationLayout.getVisibility() == View.GONE) {
                        mAnimationLayout.setVisibility(View.VISIBLE);
                    }
                }
            });

            pathAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    // clean up paths and stars
                    mView.cleanDrawings();
                    mAnimationLayout.setVisibility(View.GONE);
                    mTargetView.getLayoutParams().width = targetSize;
                    mTargetView.getLayoutParams().height = targetSize;
                    mTargetView.requestLayout();

                    mTargetShadowView.getLayoutParams().width = targetSize;
                    mTargetShadowView.getLayoutParams().height = targetSize;
                    mTargetShadowView.requestLayout();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            pathAnimator.start();
        }
    }
}
