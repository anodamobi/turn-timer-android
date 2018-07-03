package anoda.mobi.anoda_turn_timer.ui.settings

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

@InjectViewState
class SettingsPresenter : MvpPresenter<SettingsView>() {
    companion object {
        var BEEP_TIME_MINUTES = 0
        var BEEP_TIME_SECOND = 0
        var ROUND_DURATION_SECOND = 0
        var ROUND_DURATION_MINUTES = 0
    }

    fun onShareAppLink() {
        viewState.showShareAppVariant()
    }

    fun onGettingActualTime() {
        getBeforeSetTime()
        setBeforeBeepTime()
        setRoundDurationTime()
    }

    private fun getBeforeSetTime() {
        //todo here need set all time before beep and before round duration
        //todo minutes and second
    }

    fun onDataSaveAndBack() {
        //todo here need to save data
        viewState.onBackPressure()
    }

    fun onChangeBeepTimeMinutes(minutes: Int) {
        BEEP_TIME_MINUTES = minutes
        setBeforeBeepTime()
    }

    fun onChangeBeepTimeSecond(seconds: Int) {
        BEEP_TIME_SECOND = seconds
        setBeforeBeepTime()
    }

    fun onChangeRoundDurationTimeMinutes(minutes: Int) {
        ROUND_DURATION_MINUTES = minutes
        setRoundDurationTime()
    }

    fun onChangeRoundDurationTimeSecond(seconds: Int) {
        ROUND_DURATION_SECOND = seconds
        setRoundDurationTime()
    }

    private fun setRoundDurationTime() = viewState.setRoundDuration(ROUND_DURATION_MINUTES, ROUND_DURATION_SECOND)
    private fun setBeforeBeepTime() = viewState.setBeforeBeepTime(BEEP_TIME_MINUTES, BEEP_TIME_SECOND)
}