package anoda.mobi.anoda_turn_timer.util

import java.util.*

class ATimer(val timerInteraction: ATimerInteraction) {

    private class ATimerStateManager {
        var isTimerStarted = false
            private set

        var secondsToEnd = 0L
            private set
        var secondsSecondaryBeforeEnd = 0L
            private set
        private var secondsElapsed = 0L

        private var savedSecondsToEnd = 0L
        private var savedSecondsSecondaryBeforeEnd = 0L
        var secondsLeft: Long = 0L
            get() = secondsToEnd - secondsElapsed

        fun reset() {
            secondsToEnd = 0
            secondsElapsed = 0
            secondsSecondaryBeforeEnd = 0
            isTimerStarted = false
        }

        fun saveState() {
            savedSecondsToEnd = secondsLeft
            savedSecondsSecondaryBeforeEnd = secondsSecondaryBeforeEnd
        }

        fun restoreState() {
            secondsToEnd = savedSecondsToEnd
            secondsSecondaryBeforeEnd = savedSecondsSecondaryBeforeEnd

            resetSavedState()
        }

        private fun resetSavedState() {
            savedSecondsToEnd = 0L
            savedSecondsSecondaryBeforeEnd = 0L
        }

        fun setTime(secondsToEnd: Long, secondsSecondaryBeforeEnd: Long) {
            this.secondsToEnd = secondsToEnd
            this.secondsSecondaryBeforeEnd = secondsSecondaryBeforeEnd
        }

        fun setTimerStarted() {
            isTimerStarted = true
        }

        fun setTimerFinished() {
            isTimerStarted = false
        }

        fun incrementElapsedTime() {
            secondsElapsed++
        }

        fun isMainTimerFinished() = secondsLeft == 0L

        fun isSecondaryTimerFinished() = secondsLeft == secondsSecondaryBeforeEnd
    }

    companion object {
        const val TIMER_DELAY = 0L
        const val TIMER_INTERVAL = 1L * 1000
    }

    private lateinit var timer: Timer
    private val timerStateManager = ATimerStateManager()

    fun startTimer(secondsToEnd: Long, secondsSecondaryBeforeEnd: Long) {
        if (secondsToEnd < 0 || secondsSecondaryBeforeEnd < 0) {
            throw IllegalArgumentException("Seconds can't be negative")
        }

        timerStateManager.setTime(secondsToEnd, secondsSecondaryBeforeEnd)

        if (timerStateManager.isTimerStarted.not()) {
            startTimer()
        } else {
            resetTimer(secondsToEnd, secondsSecondaryBeforeEnd)
        }
    }

    fun resumeTimer() {
        timerStateManager.restoreState()

        startTimer(timerStateManager.secondsToEnd, timerStateManager.secondsSecondaryBeforeEnd)
    }

    fun pauseTimer() {
        timer.cancel()
        timer.purge()

        timerStateManager.saveState()
        timerStateManager.reset()

        timerStateManager.setTimerFinished()
    }

    fun stopTimer() {
        timer.cancel()
        timer.purge()
        timerInteraction.onMainTimerFinished()
        timerStateManager.reset()
    }

    fun resetTimer(secondsToEnd: Long, secondsSecondaryBeforeEnd: Long) {
        stopTimer()
        startTimer(secondsToEnd, secondsSecondaryBeforeEnd)
    }

    private fun startTimer() {
        timer = Timer()
        timerStateManager.setTimerStarted()

        timer.schedule(object : TimerTask() {
            override fun run() {
                if (timerStateManager.isMainTimerFinished()) {
                    stopTimer()
                    return
                }
                timerStateManager.incrementElapsedTime()
                if (timerStateManager.isSecondaryTimerFinished()) {
                    timerInteraction.onSecondaryTimerFinished()
                }
                timerInteraction.onNewTimerCycle(timerStateManager.secondsLeft)
            }
        }, TIMER_DELAY, TIMER_INTERVAL)
    }

}

interface ATimerInteraction {
    fun onMainTimerFinished()

    fun onSecondaryTimerFinished()

    fun onNewTimerCycle(timeLeft: Long)
}