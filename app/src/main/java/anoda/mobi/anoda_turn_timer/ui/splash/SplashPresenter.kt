package anoda.mobi.anoda_turn_timer.ui.splash

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
        const val DURATION_ANIMATE: Long = 1000
    }

    private lateinit var startJob: Job

    init {
        initJob()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        startAnimation()
        startTimeAndStartNextActivity()
    }

    private fun initJob() {
        startJob = launch(UI) {
            delay(START_DELAY, TimeUnit.MILLISECONDS)
            viewState.startTimerActivity()
        }
    }

    private fun startAnimation() {
        viewState.startAnimation()
    }

    private fun startTimeAndStartNextActivity() {
        startJob.start()
    }

    override fun onDestroy() {
        startJob.cancel()
        super.onDestroy()
    }
}