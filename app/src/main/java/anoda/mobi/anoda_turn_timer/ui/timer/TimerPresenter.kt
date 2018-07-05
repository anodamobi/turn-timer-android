package anoda.mobi.anoda_turn_timer.ui.timer

import android.content.Context
import anoda.mobi.anoda_turn_timer.App
import anoda.mobi.anoda_turn_timer.utils.ATimer
import anoda.mobi.anoda_turn_timer.utils.ATimerInteraction
import anoda.mobi.anoda_turn_timer.utils.SharedPreferencesManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class TimerPresenter : MvpPresenter<TimerView>(), ATimerInteraction {

    companion object {
        const val MINUTES_IN_HOUR = 60
        const val SECONDS_IN_MINUTE = 60
        const val ANGLES_IN_CIRCLE = 360

        const val READ_SETTINGS_DELAY = 500L
    }

    private var isTimerStarted = false
    private var isTimerPaused = false
    private var aTimer: ATimer = ATimer(this)
    private var currentTimerTimeToEnd = 0L //timeToEnd for current started timer

    private var innerElapsedTime = 0

    @Inject
    lateinit var context: Context

    init {
        App.appComponent.inject(this)
    }

    override fun onDestroy() {
        if (isTimerStarted || isTimerPaused)
            aTimer.stopTimer()
        super.onDestroy()
    }

    override fun attachView(view: TimerView?) {
        super.attachView(view)
        if (isTimerStarted.not() && isTimerPaused.not()) {
            launch(UI) {
                delay(READ_SETTINGS_DELAY, TimeUnit.MILLISECONDS)
                val text = formatText(getTimeToEnd())
                viewState.updateTimerText(text)
            }
        }
    }

    private fun getTimeToEnd() = SharedPreferencesManager.loadMainTimerTime(context).toLong()

    private fun getTimeToEndPlaySignal() = SharedPreferencesManager.loadSecondaryTimerTime(context).toLong()

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
        if (isTimerStarted && isTimerPaused.not()) {
            onPauseTimerClick()
        }
        viewState.startSettingsActivity()
    }

    private fun startTimer() {
        isTimerStarted = true
        isTimerPaused = false
        Timber.i("start")

        viewState.showTimerInProgress()
        viewState.showPauseButton()

        aTimer.startTimer(getTimeToEnd(), getTimeToEndPlaySignal())
        setTimeToEnd()
        viewState.playMainSignal()
    }

    private fun resetTimer() {
        aTimer.resetTimer(getTimeToEnd(), getTimeToEndPlaySignal())
        setTimeToEnd()

        isTimerStarted = true
        isTimerPaused = false

        viewState.showTimerInProgress()
        viewState.showPauseButton()
        Timber.i("reset")
    }

    private fun setTimeToEnd() {
        currentTimerTimeToEnd = getTimeToEnd()
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

        viewState.playMainSignal()
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
                return@run String.format(if (seconds >= 10) "%d:%d" else "%d:0%d", minutes, seconds)
            }
            return@run "$seconds"
        }
    }

    private fun onTimerNextIteration(timeLeftText: String) {
        viewState.updateTimerText(timeLeftText)
        viewState.updateTimerBackgroundProgress(getAngleForTimerBackground())
    }

    private fun getAngleForTimerBackground(): Float {
        val stepSize = ANGLES_IN_CIRCLE.toFloat() / currentTimerTimeToEnd
        return ANGLES_IN_CIRCLE - (innerElapsedTime * stepSize)
    }

    override fun onSecondaryTimerFinished() {
        viewState.playSecondarySignal()
    }


}