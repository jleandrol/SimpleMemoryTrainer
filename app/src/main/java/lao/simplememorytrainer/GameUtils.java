package lao.simplememorytrainer;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

/**
 * Created by leandro on 3/22/2017.
 */
public class GameUtils {

    public static Animation getAnimation(){
        final Animation animation = new AlphaAnimation(1, 0.3f);
        animation.setDuration(70);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE);
        return animation;
    }

    public static void delayMilliSeconds(int milliSeconds) {
        try {
            long s1 = System.currentTimeMillis();
            while ( ( System.currentTimeMillis() - s1 ) < milliSeconds )  {
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
