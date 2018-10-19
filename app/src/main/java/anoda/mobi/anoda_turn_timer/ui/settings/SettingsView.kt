package anoda.mobi.anoda_turn_timer.ui.settings

import android.text.Editable
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SettingsView : MvpView {
    @StateStrategyType(SkipStrategy::class)
    fun showShareAppVariant()

    fun setRoundDuration(roundTime: Editable)

    fun setBeepTime(beepTime: Editable)
}