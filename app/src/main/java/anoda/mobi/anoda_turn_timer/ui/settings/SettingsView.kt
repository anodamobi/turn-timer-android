package anoda.mobi.anoda_turn_timer.ui.settings

import com.arellomobile.mvp.MvpView

interface SettingsView : MvpView {
    fun showShareAppVariant()
    fun setRoundDuration(minutes: Int, seconds: Int)
    fun setBeforeBeepTime(minutes: Int, seconds: Int)
    fun onBackPressure()
}