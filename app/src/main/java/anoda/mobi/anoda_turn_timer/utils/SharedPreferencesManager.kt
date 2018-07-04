package anoda.mobi.anoda_turn_timer.utils

import android.content.Context
import androidx.core.content.edit
import org.jetbrains.anko.defaultSharedPreferences

object SharedPreferencesManager {

    private const val DEFAULT_TIMER_LENGTH = 60
    private const val DEFAULT_SIGNAL_LENGTH = 10

    private const val MAIN_TIMER_PREFS = "main_timer"
    private const val SECONDARY_TIMER_PREFS = "secondary_timer"

    fun saveMainTimerTime(context: Context, seconds: Int) {
        context.defaultSharedPreferences.edit(true) {
            putInt(MAIN_TIMER_PREFS, seconds)
        }
    }

    fun saveSecondaryTimerTime(context: Context, seconds: Int) {
        context.defaultSharedPreferences.edit(true) {
            putInt(SECONDARY_TIMER_PREFS, seconds)
        }
    }

    fun loadMainTimerTime(context: Context) = context.defaultSharedPreferences.getInt(MAIN_TIMER_PREFS, DEFAULT_TIMER_LENGTH)

    fun loadSecondaryTimerTime(context: Context) = context.defaultSharedPreferences.getInt(SECONDARY_TIMER_PREFS, DEFAULT_SIGNAL_LENGTH)
}