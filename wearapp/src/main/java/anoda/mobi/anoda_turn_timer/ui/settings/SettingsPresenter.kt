package anoda.mobi.anoda_turn_timer.ui.settings

import android.content.Context
import anoda.mobi.anoda_turn_timer.App
import anoda.mobi.anoda_turn_timer.utils.SharedPreferencesManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

@InjectViewState
class SettingsPresenter : MvpPresenter<SettingsView>() {

    @Inject
    lateinit var context: Context

    init {
        App.appComponent.inject(this)
    }

    companion object {
        fun getTimerPickerValues(): Array<String> = run {
            val progression = 5..100 step 5
            progression.toList().map { it.toString() }.toTypedArray()
        }

        fun getPickerPositionFromValue(value: Int): Int = getTimerPickerValues().indexOf(value.toString())

        fun getValueFromPickerPosition(index: Int): Int = getTimerPickerValues()[index].toInt()
    }

    override fun attachView(view: SettingsView?) {
        super.attachView(view)
        restoreTimeFromPrefs()
    }

    private fun restoreTimeFromPrefs() {
        val savedValue = SharedPreferencesManager.loadMainTimerTime(context)
        val positionForPicker = getPickerPositionFromValue(savedValue)
        viewState.setRoundDuration(positionForPicker)
    }

    fun saveTimeToPrefs(newPickedTimeIndex: Int) {
        val timeFromPicker = getValueFromPickerPosition(newPickedTimeIndex)
        SharedPreferencesManager.saveMainTimerTime(context, timeFromPicker)
        viewState.closeActivity()
    }

}