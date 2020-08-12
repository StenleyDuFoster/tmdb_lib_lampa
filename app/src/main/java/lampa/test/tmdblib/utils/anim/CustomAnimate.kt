package lampa.test.tmdblib.utils.anim

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import lampa.test.tmdblib.R

class CustomAnimate {

    fun scale(v: View, finalScale:Float){

        val animX = ObjectAnimator.ofFloat(v, View.SCALE_X,v.scaleX, finalScale)
        val animY = ObjectAnimator.ofFloat(v, View.SCALE_Y,v.scaleY, finalScale)
        val anim = AnimatorSet()
        anim.duration = 300
        anim.play(animX).with(animY)
        anim.start()
    }

    fun recycler(recycler: RecyclerView){

        val controller = AnimationUtils.loadLayoutAnimation(recycler.context, R.anim.rec_in)
        recycler.layoutAnimation = controller
        recycler.startLayoutAnimation()
    }

    fun alphaBlink(v: View){

        val startAlpha = v.alpha

        val animStart = ObjectAnimator.ofFloat(v, View.ALPHA,startAlpha, 1f)
        val animFinish = ObjectAnimator.ofFloat(v, View.ALPHA,1f, startAlpha)
        val anim = AnimatorSet()
        anim.duration = 200
        anim.play(animFinish).after(animStart)
        anim.start()
    }

    fun alphaFadeIn(v: View){

        val anim = ObjectAnimator.ofFloat(v, View.ALPHA,v.alpha, 1f)
        anim.duration = 400
        anim.start()
    }

    fun alphaFadeOut(v: View){

        val anim = ObjectAnimator.ofFloat(v, View.ALPHA,v.alpha, 0f)
        anim.duration = 400
        anim.start()
    }
}