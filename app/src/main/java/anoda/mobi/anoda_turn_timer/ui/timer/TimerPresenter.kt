package anoda.mobi.anoda_turn_timer.ui.timer

import android.content.Context
import anoda.mobi.anoda_turn_timer.App
import anoda.mobi.anoda_turn_timer.util.ATimer
import anoda.mobi.anoda_turn_timer.util.ATimerInteraction
import anoda.mobi.anoda_turn_timer.util.SharedPreferencesManager
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
        const val SECONDS_IN_MINUTE = 60
        const val ANGLES_IN_CIRCLE = 360

        const val READ_SETTINGS_DELAY = 500L
    }

    private var isTimerStarted = false
    private var isTimerPaused = false
    private var mATimer: ATimer = ATimer(this)
    private var mCurrentTimerTimeToEnd = 0L //timeToEnd for current started timer

    private var mInnerElapsedTime = 0

    @Inject
    lateinit var mContext: Context

    init {
        App.appComponent.inject(this)
    }

    override fun onDestroy() {
        if (isTimerStarted || isTimerPaused)
            mATimer.stopTimer()
        super.onDestroy()
    }

    override fun attachView(view: TimerView?) {
        super.attachView(view)
        launch(UI) {
            delay(READ_SETTINGS_DELAY, TimeUnit.MILLISECONDS)
            checkIsShouldUpdateTimerText()
            checkIsTimerStartedAndDurationChanged()
        }
    }

    override fun onMainTimerFinished() {
        mainTimerFinished()
        viewState.playMainSignal()
    }

    override fun onSecondaryTimerFinished() {
        viewState.playSecondarySignal()
    }

    override fun onNewTimerCycle(timeLeft: Long) {
        mInnerElapsedTime++
        val text = formatText(timeLeft)
        onTimerNextIteration(text)
    }

    fun onStartTimerClick() {
        Timber.d("Start clicked")
        if (isTimerPaused.not()) {
            startTimer()
        } else {
            resumeTimer()
        }
    }

    fun onPauseTimerClick() {
        Timber.d("Pause clicked")
        pauseTimer()
    }

    fun onResetTimerClick() {
        Timber.d("Reset clicked")
        if (isTimerStarted.not()) {
            startTimer()
        }

        if (isTimerStarted || isTimerPaused) resetTimer()
    }

    fun onSettingsClick() {
        if (isTimerStarted && isTimerPaused.not()) {
            onPauseTimerClick()
        }
        viewState.startSettingsActivity()
    }

    private fun checkIsShouldUpdateTimerText() {
        if (isTimerStarted.not() && isTimerPaused.not()) {
            updateTimerText()
        }
    }

    private fun updateTimerText() {
        val text = formatText(getTimeToEnd())
        viewState.updateTimerText(text)
    }

    private fun checkIsTimerStartedAndDurationChanged() {
        if (SharedPreferencesManager.isTimeChanged(this@TimerPresenter.mContext) && (isTimerStarted || isTimerPaused)) {
            mainTimerFinished()
            updateTimerText()
            viewState.updateTimerBackgroundProgress(ANGLES_IN_CIRCLE.toFloat())
            viewState.showTimerInProgress()
            SharedPreferencesManager.setTimeChanged(this@TimerPresenter.mContext, false)
        }
    }

    private fun getTimeToEnd() = SharedPreferencesManager.loadMainTimerTime(mContext).toLong()

    private fun getTimeToEndPlaySignal() = SharedPreferencesManager.loadSecondaryTimerTime(mContext).toLong()

    private fun startTimer() {
        isTimerStarted = true
        isTimerPaused = false
        Timber.i("start")

        viewState.showTimerInProgress()
        viewState.showPauseButton()

        mATimer.startTimer(getTimeToEnd(), getTimeToEndPlaySignal())
        setTimeToEnd()
        viewState.playMainSignal()
    }

    private fun resetTimer() {
        mATimer.resetTimer(getTimeToEnd(), getTimeToEndPlaySignal())
        setTimeToEnd()

        isTimerStarted = true
        isTimerPaused = false

        viewState.showTimerInProgress()
        viewState.showPauseButton()
        Timber.i("reset")
    }

    private fun setTimeToEnd() {
        mCurrentTimerTimeToEnd = getTimeToEnd()
    }

    private fun pauseTimer() {
        isTimerPaused = true
        Timber.i("pause")

        viewState.showStartButton()
        mATimer.pauseTimer()
    }

    private fun resumeTimer() {
        isTimerPaused = false

        Timber.i("resume")
        viewState.showPauseButton()
        mATimer.resumeTimer()
    }

    private fun mainTimerFinished() {
        isTimerStarted = false
        isTimerPaused = false

        viewState.showTimerEndProgress()
        viewState.showStartButton()

        mInnerElapsedTime = 0
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

    private fun onTimerNextIteration(timeLeftText: String) {
        viewState.showTimerInProgress()
        viewState.showPauseButton()
        viewState.updateTimerText(timeLeftText)
        viewState.updateTimerBackgroundProgress(getAngleForTimerBackground())
    }

    private fun getAngleForTimerBackground(): Float {
        val stepSize = ANGLES_IN_CIRCLE.toFloat() / mCurrentTimerTimeToEnd
        return ANGLES_IN_CIRCLE - (mInnerElapsedTime * stepSize)
    }
}