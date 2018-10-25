package anoda.mobi.anoda_turn_timer.ui.settings

import android.content.Context
import android.text.Editable
import anoda.mobi.anoda_turn_timer.App
import anoda.mobi.anoda_turn_timer.util.SharedPreferencesManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs

@InjectViewState
class SettingsPresenter : MvpPresenter<SettingsView>() {

    @Inject
    lateinit var mContext: Context

    companion object {
        private const val COLON_SYMBOL = ":"
        private const val COLON_INDEX = 2
        private const val MINIMUM_EDIT_LENGTH = 2
        private const val DOUBLE_ZERO = "00"
        private const val MINUTES_LIMIT = 59
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
            else -> setFormatEditedTime(timeValue, true)
        }
    }

    fun saveBeepTime(timeValue: String) {
        when (timeValue.length) {
            0 -> setSavedBeepTime()
            else -> setFormatEditedTime(timeValue, false)
        }
    }

    fun onTextChanged(caretPosition: Int, s: CharSequence, isRoundTime: Boolean) {
        val isCaretPositionShiftedRight = caretPosition == COLON_INDEX + 1
        val isEditSequenceHasMinimumLength = s.length > MINIMUM_EDIT_LENGTH
        val isColonNotExists = s.contains(COLON_SYMBOL).not()

        if (isCaretPositionShiftedRight && isEditSequenceHasMinimumLength && isColonNotExists) {
            val symbolsBefore = s.substring(0, COLON_INDEX)
            val symbolsAfter = s.substring(COLON_INDEX)

            val result = if (symbolsAfter.isNotEmpty()) {
                symbolsBefore + COLON_SYMBOL + symbolsAfter.substring(0, 1)
            } else {
                symbolsBefore
            }

            if (isRoundTime)
                viewState.setRoundDuration(getEditableText(result))
            else
                viewState.setBeepTime(getEditableText(result))
        }
    }

    private fun setFormatEditedTime(timeValue: String, isRoundTime: Boolean) {
        val formatTime: String
        try {
            formatTime = when (timeValue.length) {
                1 -> "0$timeValue$COLON_SYMBOL$DOUBLE_ZERO"
                2 -> "$timeValue$COLON_SYMBOL$DOUBLE_ZERO"
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
        } catch (e: Exception) {
            setSavedTime()
            Timber.e(e)
        }
    }

    private fun getTime(timeValue: String): String {
        val colonIndex = timeValue.indexOf(COLON_SYMBOL)
        val isColonPlacedOnFirstOrThirdPosition = colonIndex == 1 || colonIndex == 3
        val minutes = getMinutes(timeValue)
        val firstMinutesSymbol = timeValue.first()
        val completeMinutes = timeValue.substring(0, 2)

        val isColonExists = colonIndex < 0
        val isTimeValueHas4Symbols = timeValue.length == 4
        val isTimeValueGreaterThan4Symbols = timeValue.length > 4

        return if (isColonExists && isTimeValueHas4Symbols) {
            "$completeMinutes$COLON_SYMBOL${timeValue.substring(2)}"
        } else if (isColonExists && timeValue.length == 3) {
            "0$firstMinutesSymbol$COLON_SYMBOL${timeValue.substring(timeValue.indexOf(COLON_SYMBOL))}"
        } else if (isColonExists) {
            "$firstMinutesSymbol$COLON_SYMBOL${timeValue.substring(1)}"
        } else if (isColonPlacedOnFirstOrThirdPosition && isTimeValueHas4Symbols) {
            "$minutes$COLON_SYMBOL${getSeconds(timeValue)}"
        } else if (isColonPlacedOnFirstOrThirdPosition && isTimeValueGreaterThan4Symbols) {
            getTime(correctTime(timeValue))
        } else if (isColonExists && isTimeValueGreaterThan4Symbols && (colonIndex == 3 || colonIndex == 4)) {
            getTime(completeMinutes + COLON_SYMBOL + timeValue.substring(MINUTES_END_INDEX, timeValue.indexOf(COLON_SYMBOL)))
        } else if (timeValue.contains(COLON_SYMBOL).not() && isTimeValueHas4Symbols) {
            timeValue.substring(0, MINUTES_END_INDEX) + COLON_SYMBOL + getSeconds(timeValue.substring(MINUTES_END_INDEX))
        } else {
            "$minutes$COLON_SYMBOL${getSeconds(timeValue)}"
        }
    }

    private fun getMinutes(timeValue: String): String {
        var minutes = timeValue

        minutes = if (minutes.contains(COLON_SYMBOL)) {
            minutes.substring(0, minutes.indexOf(COLON_SYMBOL))
        } else {
            minutes.substring(0, MINUTES_END_INDEX)
        }

        minutes = when {
            minutes.isEmpty() -> DOUBLE_ZERO
            minutes.length == 1 -> "0$minutes"
            minutes.toInt() > MINUTES_LIMIT -> MINUTES_LIMIT.toString()
            else -> minutes
        }

        return minutes
    }

    private fun getSeconds(timeValue: String): String {
        var seconds = timeValue

        seconds = if (seconds.contains(COLON_SYMBOL)) {
            seconds.substring(seconds.indexOf(COLON_SYMBOL) + 1)
        } else {
            seconds.substring(MINUTES_END_INDEX)
        }

        seconds = when {
            seconds.isEmpty() -> DOUBLE_ZERO
            seconds.length == 1 -> "0$seconds"
            seconds.toInt() > SECONDS_LIMIT -> SECONDS_LIMIT.toString()
            else -> seconds
        }
        return seconds
    }

    private fun correctTime(timeValue: String): String {
        val colonIndex = timeValue.indexOf(COLON_SYMBOL)
        val isColonPlacedCorrectly = colonIndex == COLON_INDEX

        return if (isColonPlacedCorrectly) getTime(timeValue)
        else moveColonSymbol(timeValue, colonIndex)
    }

    private fun moveColonSymbol(timeValue: String, colonIndex: Int): String {
        val moveStepsCont = COLON_INDEX - colonIndex

        return when (colonIndex) {
            1 -> moveColonForward(timeValue, colonIndex, moveStepsCont)
            else -> moveColonBackward(timeValue, colonIndex, abs(moveStepsCont))
        }
    }

    private fun moveColonForward(timeValue: String, colonIndex: Int, steps: Int): String {
        val symbolsBefore = timeValue.substring(0, colonIndex)
        val symbolsAfterStartPosition = colonIndex + 1
        val symbolsAfter = timeValue.substring(symbolsAfterStartPosition)

        val updSymbolsBefore = symbolsBefore + symbolsAfter[0]
        val updSymbolsAfter = symbolsAfter.substring(steps)

        return "$updSymbolsBefore$COLON_SYMBOL$updSymbolsAfter"
    }

    private fun moveColonBackward(timeValue: String, colonIndex: Int, steps: Int): String {
        val symbolsBefore = timeValue.substring(0, colonIndex)
        val symbolsAfterStartPosition = colonIndex + 1
        val symbolsAfter = timeValue.substring(symbolsAfterStartPosition)

        val symbolsBeforeEndPosition = colonIndex - steps
        val updSymbolsBefore = symbolsBefore.substring(0, symbolsBeforeEndPosition)
        val updSymbolsAfter = symbolsBefore.last() + symbolsAfter

        return "$updSymbolsBefore$COLON_SYMBOL$updSymbolsAfter"
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