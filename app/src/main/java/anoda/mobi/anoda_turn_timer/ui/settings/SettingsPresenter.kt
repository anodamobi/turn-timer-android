package anoda.mobi.anoda_turn_timer.ui.settings

import android.content.Context
import anoda.mobi.anoda_turn_timer.App
import anoda.mobi.anoda_turn_timer.ui.timer.TimerPresenter.Companion.SECONDS_IN_MINUTE
import anoda.mobi.anoda_turn_timer.utils.SharedPreferencesManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

@InjectViewState
class SettingsPresenter : MvpPresenter<SettingsView>() {

    @Inject
    lateinit var context: Context

    private var beepTimeMinutes = 0
    private var beepTimeSeconds = 0
    private var roundDurationMinutes = 0
    private var roundDurationSeconds = 0

    init {
        App.appComponent.inject(this)
    }

    fun onShareAppLink() {
        viewState.showShareAppVariant()
    }

    private fun onGettingActualTime() {
        getBeforeSetTime()
        setBeforeBeepTime()
        setRoundDurationTime()
    }

    override fun attachView(view: SettingsView?) {
        super.attachView(view)
        onGettingActualTime()
    }

    override fun detachView(view: SettingsView?) {
        saveTimeToPrefs()
        super.detachView(view)
    }

    private fun getBeforeSetTime() {
        beepTimeMinutes = SharedPreferencesManager.loadSecondaryTimerTime(context) / SECONDS_IN_MINUTE
        beepTimeSeconds = SharedPreferencesManager.loadSecondaryTimerTime(context) - (beepTimeMinutes * SECONDS_IN_MINUTE)
        roundDurationMinutes = SharedPreferencesManager.loadMainTimerTime(context) / SECONDS_IN_MINUTE
        roundDurationSeconds = SharedPreferencesManager.loadMainTimerTime(context) - (roundDurationMinutes * SECONDS_IN_MINUTE)
    }

    private fun saveTimeToPrefs() {
        val mainTimerSeconds = roundDurationMinutes * SECONDS_IN_MINUTE + roundDurationSeconds
        var secondTimerSeconds = beepTimeMinutes * SECONDS_IN_MINUTE + beepTimeSeconds
        if (secondTimerSeconds > mainTimerSeconds) {
            secondTimerSeconds = mainTimerSeconds
        }
        SharedPreferencesManager.saveMainTimerTime(context, mainTimerSeconds)
        SharedPreferencesManager.saveSecondaryTimerTime(context, secondTimerSeconds)
    }

    fun onChangeBeepTimeMinutes(minutes: Int) {
        beepTimeMinutes = minutes
        setBeforeBeepTime()
    }

    fun onChangeBeepTimeSecond(seconds: Int) {
        beepTimeSeconds = seconds
        setBeforeBeepTime()
    }

    fun onChangeRoundDurationTimeMinutes(minutes: Int) {
        roundDurationMinutes = minutes
        setRoundDurationTime()
    }

    fun onChangeRoundDurationTimeSecond(seconds: Int) {
        roundDurationSeconds = seconds
        setRoundDurationTime()
    }

    private fun setRoundDurationTime() = viewState.setRoundDuration(roundDurationMinutes, roundDurationSeconds)
    private fun setBeforeBeepTime() = viewState.setBeforeBeepTime(beepTimeMinutes, beepTimeSeconds)
}