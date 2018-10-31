package anoda.mobi.anoda_turn_timer.util

import android.content.Context
import androidx.core.content.edit
import org.jetbrains.anko.defaultSharedPreferences

object SharedPreferencesManager {

    private const val DEFAULT_TIMER_LENGTH = 60
    private const val DEFAULT_SIGNAL_LENGTH = 10

    private const val MAIN_TIMER_PREFS = "main_timer"
    private const val SECONDARY_TIMER_PREFS = "secondary_timer"
    private const val IS_TIME_CHANGED = "is_time_changed"

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

    fun setTimeChanged(context: Context, value: Boolean) {
        context.defaultSharedPreferences.edit(true) {
            putBoolean(IS_TIME_CHANGED, value)
        }
    }

    fun loadMainTimerTime(context: Context) = context.defaultSharedPreferences.getInt(MAIN_TIMER_PREFS, DEFAULT_TIMER_LENGTH)

    fun loadSecondaryTimerTime(context: Context) = context.defaultSharedPreferences.getInt(SECONDARY_TIMER_PREFS, DEFAULT_SIGNAL_LENGTH)

    fun isTimeChanged(context: Context) = context.defaultSharedPreferences.getBoolean(IS_TIME_CHANGED, false)
}