package anoda.mobi.anoda_turn_timer.ui.settings

import android.content.Context
import android.text.Editable
import anoda.mobi.anoda_turn_timer.App
import anoda.mobi.anoda_turn_timer.util.SharedPreferencesManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

@InjectViewState
class SettingsPresenter : MvpPresenter<SettingsView>() {

    @Inject
    lateinit var mContext: Context

    companion object {
        private const val COLON_SYMBOL = ":"
        private const val DOUBLE_ZERO = "00"
        private const val MINUTES_IN_HOUR = 60
        private const val SECONDS_IN_MINUTE = 60
        private const val SECONDS_LIMIT = 59
        private const val MINUTES_END_INDEX = 2
        private const val SECONDS_START_INDEX = 3
    }

    init {
        App.appComponent.inject(this)
    }

    override fun attachView(view: SettingsView?) {
        super.attachView(view)
        setSavedTime()
        SharedPreferencesManager.setTimeChanged(mContext, false)
    }

    fun onShareAppLink() {
        viewState.showShareAppVariant()
    }

    fun saveRoundTime(timeValue: String) {
        when (timeValue.length) {
            0 -> setSavedRoundTime()
            else -> setFormatEditedTime2(timeValue, true)
        }
    }

    fun saveBeepTime(timeValue: String) {
        when (timeValue.length) {
            0 -> setSavedBeepTime()
            else -> setFormatEditedTime2(timeValue, false)
        }
    }

    private fun setFormatEditedTime2(timeValue: String, isRoundTime: Boolean) {
        val formatTime = when (timeValue.length) {
            1 -> "0$timeValue$COLON_SYMBOL$DOUBLE_ZERO"
            2 -> "$timeValue$COLON_SYMBOL$DOUBLE_ZERO"
            3 -> "${timeValue.substring(0, MINUTES_END_INDEX)}$COLON_SYMBOL$DOUBLE_ZERO"
            else -> getTime(timeValue)
        }
        val formatTimeEditable = getEditableText(formatTime)

        if (isRoundTime) {
            viewState.setRoundDuration(formatTimeEditable)
            saveNewTime(formatTime, true)
        } else {
            viewState.setBeepTime(formatTimeEditable)
            saveNewTime(formatTime, false)
        }
        SharedPreferencesManager.setTimeChanged(mContext, true)
    }

    private fun getTime(timeValue: String): String {
        val minutes = getMinutes(timeValue)
        val seconds = getSeconds(timeValue)
        return "$minutes$COLON_SYMBOL$seconds"
    }

    private fun getMinutes(timeValue: String): String {
        var minutes = timeValue
        if (timeValue.contains(COLON_SYMBOL)) {
            minutes = minutes.substring(0, minutes.indexOf(COLON_SYMBOL))
        }
        minutes = if (minutes.length == 1) "0${minutes}" else minutes
        return minutes
    }

    private fun getSeconds(timeValue: String): String {
        var seconds = timeValue
        if (timeValue.contains(COLON_SYMBOL)) {
            seconds = seconds.substring(seconds.indexOf(COLON_SYMBOL) + 1)
        }
        seconds = when {
            seconds.length == 1 -> "${seconds}0"
            seconds.toInt() > SECONDS_LIMIT -> SECONDS_LIMIT.toString()
            else -> seconds
        }
        return seconds
    }

    private fun saveNewTime(timeValue: String, isRoundTime: Boolean) {
        val timeInSeconds = when (timeValue.length) {
            1, 2 -> timeValue.toInt() * SECONDS_IN_MINUTE
            else -> getTimeInSeconds(timeValue.substring(0, MINUTES_END_INDEX).toInt(),
                    timeValue.substring(SECONDS_START_INDEX).toInt())
        }

        val savedRoundSeconds = SharedPreferencesManager.loadMainTimerTime(mContext)
        val savedBeepSeconds = SharedPreferencesManager.loadSecondaryTimerTime(mContext)

        if (isRoundTime) {
            if (timeInSeconds < savedBeepSeconds) {
                SharedPreferencesManager.saveSecondaryTimerTime(mContext, timeInSeconds)
            } else {
                SharedPreferencesManager.saveMainTimerTime(mContext, timeInSeconds)
            }
        } else {
            if (timeInSeconds > savedRoundSeconds) {
                SharedPreferencesManager.saveSecondaryTimerTime(mContext, savedRoundSeconds)
            } else {
                SharedPreferencesManager.saveSecondaryTimerTime(mContext, timeInSeconds)
            }
        }
    }

    private fun getTimeInSeconds(minutesValue: Int, secondsValue: Int): Int {
        val minutesInt = minutesValue * SECONDS_IN_MINUTE
        return minutesInt + secondsValue
    }

    private fun setSavedTime() {
        setSavedRoundTime()
        setSavedBeepTime()
    }

    private fun setSavedRoundTime() {
        val timeInSeconds = SharedPreferencesManager.loadMainTimerTime(mContext)
        val minutes = timeInSeconds / SECONDS_IN_MINUTE
        val seconds = timeInSeconds - (minutes * SECONDS_IN_MINUTE)

        val minutesStr = formatSavedTime(minutes.toString())
        val secondsStr = formatSavedTime(seconds.toString())

        val timeResultEditable = getEditableText(minutesStr + COLON_SYMBOL + secondsStr)

        viewState.setRoundDuration(timeResultEditable)
    }

    private fun setSavedBeepTime() {
        val timeInSeconds = SharedPreferencesManager.loadSecondaryTimerTime(mContext)
        val minutes = timeInSeconds / SECONDS_IN_MINUTE
        val seconds = timeInSeconds - (minutes * SECONDS_IN_MINUTE)

        val minutesStr = formatSavedTime(minutes.toString())
        val secondsStr = formatSavedTime(seconds.toString())

        val timeResultEditable = getEditableText(minutesStr + COLON_SYMBOL + secondsStr)

        viewState.setBeepTime(timeResultEditable)
    }

    private fun formatSavedTime(timeValue: String): String = when (timeValue.length) {
        1 -> "0$timeValue"
        else -> timeValue
    }

    private fun getEditableText(value: String): Editable = Editable.Factory.getInstance().newEditable(value)
}