package anoda.mobi.anoda_turn_timer.ui.timer

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import timber.log.Timber

@InjectViewState
class TimerPresenter : MvpPresenter<TimerView>() {

    private var isTimerStarted = false
    private var isTimerPaused = false

    fun onStartTimerClick() {
        if (isTimerPaused.not()) {
            startTimer()
        } else {
            resumeTimer()
        }
    }

    fun onPauseTimerClick() {
        pauseTimer()
    }

    fun onResetTimerClick() {
        resetTimer()
    }

    fun onTimerTextClick() {
        resetTimer()
    }

    fun onSettingsClick() {
        viewState.startSettingsActivity()
    }

    private fun startTimer() {
        isTimerStarted = true
        isTimerPaused = false
        Timber.i("start")

        viewState.showTimerInProgress()
        viewState.showPauseButton()
    }

    private fun resetTimer() {
        Timber.i("reset")
        startTimer()


    }

    private fun pauseTimer() {
        isTimerPaused = true
        Timber.i("pause")

        viewState.showStartButton()
    }

    private fun resumeTimer() {
        isTimerPaused = false

        Timber.i("resume")
        viewState.showPauseButton()
    }

    private fun onTimerFinished() {
        isTimerStarted = false
        isTimerPaused = false

        viewState.showTimerEndProgress()
        viewState.showStartButton()
    }

    private fun onTimerNextIteration() {
        viewState.updateTimerText("1234")
    }

}