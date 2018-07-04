package anoda.mobi.anoda_turn_timer.ui.splash

import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit

@InjectViewState
class SplashPresenter : MvpPresenter<SplashView>() {

    companion object {
        private const val START_DELAY: Long = 1300
        private const val DURATION_ANIMATE: Long = 1000
    }

    private lateinit var startJob: Job

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        startAnimation()
        startTimeAndStartNextActivity()
    }

    private fun startAnimation() {
        val rotate = RotateAnimation(180f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate.duration = DURATION_ANIMATE
        viewState.startAnimation(rotate)
    }

    private fun startTimeAndStartNextActivity() {
        startJob = launch(UI) {
            delay(START_DELAY, TimeUnit.MILLISECONDS)
            viewState.startTimerActivity()
        }
    }

    override fun onDestroy() {
        startJob.cancel()
        super.onDestroy()
    }
}