package anoda.mobi.anoda_turn_timer.ui.timer

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface TimerView : MvpView {

    fun startSettingsActivity()

    fun showPauseButton()

    fun showStartButton()

    fun showTimerInProgress()

    fun showTimerEndProgress()

    fun updateTimerText(text: String)

    @StateStrategyType(SkipStrategy::class)
    fun playSignal()

    fun updateTimerBackgroundProgress(angle: Float)
}