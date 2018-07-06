package anoda.mobi.anoda_turn_timer.ui.main

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {

    fun showTimerInProgress()

    fun showTimerPauseProgress()

    fun onTimerFinished()

    fun updateTimerText(text: String)

    @StateStrategyType(SkipStrategy::class)
    fun startSettingsActivity()
}