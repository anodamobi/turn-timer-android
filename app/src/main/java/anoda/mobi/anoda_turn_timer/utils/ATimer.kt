package anoda.mobi.anoda_turn_timer.utils

import java.util.*

class ATimer(val timerInteraction: ATimerInteraction) {

    companion object {
        const val TIMER_DELAY = 0L
        const val TIMER_INTERVAL = 1L * 1000
    }

    var isTimerStarted = false
        private set
    var isTimerPaused = false
        private set

    private var secondsToEnd = 0L
    private var secondsElapsed = 0L
    private var secondsLeft: Long = 0L
        get() = secondsToEnd - secondsElapsed


    private lateinit var timer: Timer

    public fun startTimer(secondsToEnd: Long) {
        if (secondsToEnd < 0) {
            throw IllegalArgumentException("Seconds can't be negative")
        }

        this.secondsToEnd = secondsToEnd

        if (isTimerStarted.not()) {
            startTimer()
        } else {
            resetTimer(secondsToEnd)
        }
    }

    public fun resumeTimer() {
        //todo implement
    }

    public fun pauseTimer() {
        //todo implement
    }

    public fun stopTimer() {
        timer.cancel()
        timer.purge()
        timerInteraction.onTimerFinished()
        resetCounters()
    }

    public fun resetTimer(secondsToEnd: Long) {
        stopTimer()
        startTimer(secondsToEnd)
    }

    private fun startTimer() {
        timer = Timer()

        timer.schedule(object : TimerTask() {
            override fun run() {
                secondsElapsed++
                if (secondsLeft == 0L) {
                    stopTimer()
                    return
                }
                timerInteraction.onNewTimerCycle(secondsLeft)
            }
        }, TIMER_DELAY, TIMER_INTERVAL)
    }

    private fun resetCounters() {
        secondsToEnd = 0
        secondsElapsed = 0
        isTimerPaused = false
        isTimerStarted = false
    }
}

interface ATimerInteraction {
    fun onTimerFinished()

    fun onNewTimerCycle(timeLeft: Long)
}