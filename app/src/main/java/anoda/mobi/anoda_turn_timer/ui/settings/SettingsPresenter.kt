package anoda.mobi.anoda_turn_timer.ui.settings

import android.content.Context
import anoda.mobi.anoda_turn_timer.App
import anoda.mobi.anoda_turn_timer.ui.timer.TimerPresenter.Companion.SECONDS_IN_MINUTE
import anoda.mobi.anoda_turn_timer.util.SharedPreferencesManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

@InjectViewState
class SettingsPresenter : MvpPresenter<SettingsView>() {

    @Inject
    lateinit var mContext: Context

    private var mBeepTimeMinutes = 0
    private var mBeepTimeSeconds = 0
    private var mRoundDurationMinutes = 0
    private var mRoundDurationSeconds = 0

    init {
        App.appComponent.inject(this)
    }

    override fun attachView(view: SettingsView?) {
        super.attachView(view)
        onGettingActualTime()
        SharedPreferencesManager.setTimeChanged(mContext, false)
    }

    override fun detachView(view: SettingsView?) {
        saveTimeToPrefs()
        super.detachView(view)
    }

    fun onShareAppLink() {
        viewState.showShareAppVariant()
    }

    fun onChangeBeepTimeMinutes(minutes: Int) {
        mBeepTimeMinutes = minutes
        setBeforeBeepTime()
    }

    fun onChangeBeepTimeSecond(seconds: Int) {
        mBeepTimeSeconds = seconds
        setBeforeBeepTime()
    }

    fun onChangeRoundDurationTimeMinutes(minutes: Int) {
        mRoundDurationMinutes = minutes
        setRoundDurationTime()
    }

    fun onChangeRoundDurationTimeSecond(seconds: Int) {
        mRoundDurationSeconds = seconds
        setRoundDurationTime()
    }

    fun setRoundDurationTime() = viewState.setRoundDuration(mRoundDurationMinutes, mRoundDurationSeconds)

    fun setBeforeBeepTime() = viewState.setBeforeBeepTime(mBeepTimeMinutes, mBeepTimeSeconds)

    private fun onGettingActualTime() {
        getBeforeSetTime()
        setBeforeBeepTime()
        setRoundDurationTime()
    }

    private fun getBeforeSetTime() {
        mBeepTimeMinutes = SharedPreferencesManager.loadSecondaryTimerTime(mContext) / SECONDS_IN_MINUTE
        mBeepTimeSeconds = SharedPreferencesManager.loadSecondaryTimerTime(mContext) - (mBeepTimeMinutes * SECONDS_IN_MINUTE)
        mRoundDurationMinutes = SharedPreferencesManager.loadMainTimerTime(mContext) / SECONDS_IN_MINUTE
        mRoundDurationSeconds = SharedPreferencesManager.loadMainTimerTime(mContext) - (mRoundDurationMinutes * SECONDS_IN_MINUTE)
    }

    private fun saveTimeToPrefs() {
        val newMainTimerSeconds = mRoundDurationMinutes * SECONDS_IN_MINUTE + mRoundDurationSeconds
        var secondTimerSeconds = mBeepTimeMinutes * SECONDS_IN_MINUTE + mBeepTimeSeconds
        if (secondTimerSeconds > newMainTimerSeconds) {
            secondTimerSeconds = newMainTimerSeconds
        }

        val previousMainTimerValue = SharedPreferencesManager.loadMainTimerTime(mContext)
        if (previousMainTimerValue != newMainTimerSeconds) {
            SharedPreferencesManager.setTimeChanged(mContext, true)
        }

        SharedPreferencesManager.saveMainTimerTime(mContext, newMainTimerSeconds)
        SharedPreferencesManager.saveSecondaryTimerTime(mContext, secondTimerSeconds)
    }
}