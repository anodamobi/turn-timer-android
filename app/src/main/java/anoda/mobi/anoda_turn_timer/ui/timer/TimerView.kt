package anoda.mobi.anoda_turn_timer.ui.timer

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface TimerView: MvpView {

    fun startSettingsActivity()

    fun showPauseButton()

    fun showStartButton()

    fun showTimerInProgress()

    fun showTimerEndProgress()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateTimerText(text: String)
}