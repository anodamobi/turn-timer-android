package anoda.mobi.anoda_turn_timer.ui.timer

import com.arellomobile.mvp.MvpView

interface TimerView: MvpView {

    fun startSettingsActivity()

    fun showPauseButton()

    fun showStartButton()

    fun showTimerInProgress()

    fun showTimerEndProgress()

    fun updateTimerText(text: String)

}