package anoda.mobi.anoda_turn_timer.ui.main

import anoda.mobi.anoda_turn_timer.utils.ATimer
import anoda.mobi.anoda_turn_timer.utils.ATimerInteraction
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import java.util.concurrent.TimeUnit

@InjectViewState
class MainPresenter : MvpPresenter<MainView>(), ATimerInteraction {

    companion object {
        const val SECONDS_IN_MINUTE = 60
    }

    private var isTimerStarted = false
    private var isTimerPaused = false
    private var aTimer: ATimer = ATimer(this)

    fun onSettingsClick() {
        viewState.startSettingsActivity()
    }

    override fun onDestroy() {
        if (isTimerStarted || isTimerPaused)
            aTimer.stopTimer()
        super.onDestroy()
    }

    fun onTimerClick() {
        if (isTimerStarted.not()) {
            startTimer()
            return
        }
        if (isTimerPaused) {
            resumeTimer()
        } else {
            pauseTimer()
        }
    }

    override fun attachView(view: MainView?) {
        super.attachView(view)
        updateTimerText()
    }

    private fun updateTimerText() {
        val text = formatText(getTimeToEnd())
        viewState.updateTimerText(text)
    }

    private fun getTimeToEnd() = 10L

    private fun getTimeToEndPlaySignal() = 0L

    private fun startTimer() {
        isTimerStarted = true
        isTimerPaused = false

        viewState.showTimerInProgress()

        aTimer.startTimer(getTimeToEnd(), getTimeToEndPlaySignal())
    }

    private fun pauseTimer() {
        isTimerPaused = true

        viewState.showTimerPauseProgress()
        aTimer.pauseTimer()
    }

    private fun resumeTimer() {
        isTimerPaused = false

        viewState.showTimerInProgress()
        aTimer.resumeTimer()
    }

    override fun onMainTimerFinished() {
        mainTimerFinished()
    }

    private fun mainTimerFinished() {
        isTimerStarted = false
        isTimerPaused = false

        viewState.onTimerFinished()
    }

    override fun onSecondaryTimerFinished() {
        //do nothing
    }

    override fun onNewTimerCycle(timeLeft: Long) {
        val text = formatText(timeLeft)
        onTimerNextIteration(text)
    }

    private fun onTimerNextIteration(timeLeftText: String) {
        viewState.updateTimerText(timeLeftText)
    }

    private fun formatText(timeLeft: Long): String {
        val minutes = TimeUnit.SECONDS.toMinutes(timeLeft)
        val seconds = timeLeft - minutes * SECONDS_IN_MINUTE
        return run {
            if (minutes > 0) {
                return@run String.format(if (seconds >= 10) "%d:%d" else "%d:0%d", minutes, seconds)
            }
            return@run "$seconds"
        }
    }

}