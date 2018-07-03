package anoda.mobi.anoda_turn_timer.ui.timer

import anoda.mobi.anoda_turn_timer.utils.ATimer
import anoda.mobi.anoda_turn_timer.utils.ATimerInteraction
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import timber.log.Timber

@InjectViewState
class TimerPresenter : MvpPresenter<TimerView>(), ATimerInteraction {

    private var isTimerStarted = false
    private var isTimerPaused = false
    private var aTimer: ATimer = ATimer(this)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        val text = formatText(getTimeToEnd())
        viewState.updateTimerText(text)
    }

    override fun detachView(view: TimerView?) {
        aTimer.stopTimer()
        super.detachView(view)
    }

    //todo get from prefs
    private fun getTimeToEnd(): Long {
        return 4
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

        aTimer.startTimer(getTimeToEnd())
    }

    private fun resetTimer() {
        aTimer.resetTimer(getTimeToEnd())

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

    override fun onTimerFinished() {
        isTimerStarted = false
        isTimerPaused = false

        viewState.showTimerEndProgress()
        viewState.showStartButton()
    }

    override fun onNewTimerCycle(timeLeft: Long) {
        val text = formatText(timeLeft)
        onTimerNextIteration(text)
    }

    private fun formatText(timeLeft: Long): String {
        return timeLeft.toString()
    }

    private fun onTimerNextIteration(timeLeftText: String) {
        viewState.updateTimerText(timeLeftText)
    }

}