package anoda.mobi.anoda_turn_timer.ui.timer

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface TimerView : MvpView {

    @StateStrategyType(SkipStrategy::class)
    fun startSettingsActivity()

    fun showPauseButton()

    fun showStartButton()

    fun showTimerInProgress()

    fun showTimerEndProgress()

    fun updateTimerText(text: String)

    @StateStrategyType(SkipStrategy::class)
    fun playMainSignal()

    @StateStrategyType(SkipStrategy::class)
    fun playSecondarySignal()

    fun updateTimerBackgroundProgress(angle: Float)
}