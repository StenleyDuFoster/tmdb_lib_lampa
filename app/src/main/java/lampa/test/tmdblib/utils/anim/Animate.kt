package lampa.test.tmdblib.utils.anim

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.RecyclerView

import lampa.test.tmdblib.R

class Animate {

    fun scale(v: View, finalScale:Float){

        var animX = ObjectAnimator.ofFloat(v, View.SCALE_X,v.scaleX, finalScale)
        var animY = ObjectAnimator.ofFloat(v, View.SCALE_Y,v.scaleY, finalScale)
        var anim = AnimatorSet()
        anim.setDuration(300)
        anim.play(animX).with(animY)
        anim.start()
    }

    fun recycler(recycler: RecyclerView){

        val controller = AnimationUtils.loadLayoutAnimation(recycler.context, R.anim.rec_in)
        recycler.setLayoutAnimation(controller)
        recycler.startLayoutAnimation()
    }
}