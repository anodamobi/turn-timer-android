package anoda.mobi.anoda_turn_timer.ui.splash

import android.view.animation.RotateAnimation
import com.arellomobile.mvp.MvpView

interface SplashView : MvpView {

    fun startTimerActivity()
    fun startAnimation(animator: RotateAnimation)
}