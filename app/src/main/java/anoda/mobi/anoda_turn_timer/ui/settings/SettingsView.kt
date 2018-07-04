package anoda.mobi.anoda_turn_timer.ui.settings

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SettingsView : MvpView {
    @StateStrategyType(SkipStrategy::class)
    fun showShareAppVariant()
    fun setRoundDuration(minutes: Int, seconds: Int)
    fun setBeforeBeepTime(minutes: Int, seconds: Int)
}