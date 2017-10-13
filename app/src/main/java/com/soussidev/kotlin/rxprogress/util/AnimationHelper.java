package com.soussidev.kotlin.rxprogress.util;

import android.view.View;


/**
 * Created by Soussi on 12/10/2017.
 */

public class AnimationHelper {

    public static AnimationScale animationScale;

    public AnimationHelper() {
    }

    /**
    * @author soussi
     * @apiNote 26
     * @param animationScale
     * @Info Init Constructor
    * */

    public AnimationHelper(AnimationScale animationScale) {
       this.animationScale=animationScale;

    }

    /**
    * @author soussi
     * @param views
    * */

    public static void animateGroup(View... views) {
        int startTime = 300;

        for (View v : views) {

            transSwitch(v, startTime);
            startTime += 100;
        }


    }

    /**
    * @author soussi
     * @Fun transSwitch()
     * @param view
     * @param delay
    * */
    private static void transSwitch(View view, long delay)
    {
       if(animationScale.equals(AnimationScale.TranslationRight))
       {
           quickViewRevealX(view, delay, 1f);

       }else if(animationScale.equals(AnimationScale.TranslationLeft))
       {
           quickViewRevealX(view, delay, -1f);
       }

       else if(animationScale.equals(AnimationScale.TransLationTop))
       {
           quickViewRevealY(view, delay, -1f);
       }

       else if(animationScale.equals(AnimationScale.TransLationEnd))
       {
           quickViewRevealY(view, delay, 1f);
       }


    }

/**
* @author soussi
 * @param delay
 * @param view
 * @param trans
 * @Info Animation start in Axis X
* */

    public static void quickViewRevealX(View view, long delay,float trans) {
        view.setTranslationX(trans * DensityConverter.INSTANCE.toDp(view.getContext(), 26));
        view.setAlpha(0f);

        view.setVisibility(View.VISIBLE);

        view.animate()
                .translationX(0f)
                .alpha(1f)
                .setStartDelay(delay)
                .start();
    }

    /**
    * @author soussi
     * @param trans
     * @param view
     * @param delay
     * @Info Animation start in Axis Y
    * */

    public static void quickViewRevealY(View view, long delay,float trans) {
        view.setTranslationY(trans * DensityConverter.INSTANCE.toDp(view.getContext(), 26));
        view.setAlpha(0f);

        view.setVisibility(View.VISIBLE);

        view.animate()
                .translationY(0f)
                .alpha(1f)
                .setStartDelay(delay)
                .start();
    }





}
