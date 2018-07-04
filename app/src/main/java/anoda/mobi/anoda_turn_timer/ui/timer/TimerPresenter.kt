package anoda.mobi.anoda_turn_timer.ui.timer

import anoda.mobi.anoda_turn_timer.utils.ATimer
import anoda.mobi.anoda_turn_timer.utils.ATimerInteraction
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import timber.log.Timber
import java.util.concurrent.TimeUnit

@InjectViewState
class TimerPresenter : MvpPresenter<TimerView>(), ATimerInteraction {

    companion object {
        const val MINUTES_IN_HOUR = 60
        const val ANGLES_IN_CIRCLE = 360
    }

    private var isTimerStarted = false
    private var isTimerPaused = false
    private var aTimer: ATimer = ATimer(this)

    private var innerElapsedTime = 0

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        val text = formatText(getTimeToEnd())
        viewState.updateTimerText(text)
    }

    override fun onDestroy() {
        if (isTimerStarted || isTimerPaused)
            aTimer.stopTimer()
        super.onDestroy()
    }

    //todo get from prefs
    private fun getTimeToEnd(): Long {
        return 65
    }

    //todo get from prefs
    private fun getTimeToEndPlaySignal(): Long {
        return 3
    }

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
        if (isTimerStarted.not()) {
            startTimer()
        } else {
            resetTimer()
        }
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

        aTimer.startTimer(getTimeToEnd(), getTimeToEndPlaySignal())
    }

    private fun resetTimer() {
        aTimer.resetTimer(getTimeToEnd(), getTimeToEndPlaySignal())

        isTimerStarted = true
        isTimerPaused = false

        viewState.showTimerInProgress()
        viewState.showPauseButton()
        Timber.i("reset")
    }

    private fun pauseTimer() {
        isTimerPaused = true
        Timber.i("pause")

        viewState.showStartButton()
        aTimer.pauseTimer()
    }

    private fun resumeTimer() {
        isTimerPaused = false

        Timber.i("resume")
        viewState.showPauseButton()
        aTimer.resumeTimer()
    }

    override fun onMainTimerFinished() {
        isTimerStarted = false
        isTimerPaused = false

        viewState.showTimerEndProgress()
        viewState.showStartButton()

        innerElapsedTime = 0
    }

    override fun onNewTimerCycle(timeLeft: Long) {
        innerElapsedTime++
        val text = formatText(timeLeft)
        onTimerNextIteration(text)
    }

    private fun formatText(timeLeft: Long): String {
        val minutes = TimeUnit.SECONDS.toMinutes(timeLeft)
        val seconds = timeLeft - minutes * MINUTES_IN_HOUR
        return run {
            if (minutes > 0) {
                return@run String.format(if (seconds > 10) "%d:%d" else "%d:0%d", minutes, seconds)
            }
            return@run "$seconds"
        }
    }

    private fun onTimerNextIteration(timeLeftText: String) {
        viewState.updateTimerText(timeLeftText)
        viewState.updateTimerBackgroundProgress(getAngleForTimerBackground())
    }

    private fun getAngleForTimerBackground(): Float {
        val stepSize = ANGLES_IN_CIRCLE.toFloat() / getTimeToEnd()
        return ANGLES_IN_CIRCLE - (innerElapsedTime * stepSize)
    }

    override fun onSecondaryTimerFinished() {
        viewState.playSignal()
    }
}