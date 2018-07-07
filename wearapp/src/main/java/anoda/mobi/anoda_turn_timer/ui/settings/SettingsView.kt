package anoda.mobi.anoda_turn_timer.ui.settings

import com.arellomobile.mvp.MvpView

interface SettingsView : MvpView {

    fun setRoundDuration(position: Int)

    fun closeActivity()
}