package anoda.mobi.anoda_turn_timer.utils

import java.util.*

class ATimer(val timerInteraction: ATimerInteraction) {

    companion object {
        const val TIMER_DELAY = 0L
        const val TIMER_INTERVAL = 1L * 1000
    }

    var isTimerStarted = false
        private set

    private var secondsToEnd = 0L
    private var secondsSecondaryBeforeEnd = 0L
    private var secondsElapsed = 0L

    private var secondsLeft: Long = 0L
        get() = secondsToEnd - secondsElapsed


    private lateinit var timer: Timer

    public fun startTimer(secondsToEnd: Long, secondsSecondaryBeforeEnd: Long) {
        if (secondsToEnd < 0) {
            throw IllegalArgumentException("Seconds can't be negative")
        }

        this.secondsToEnd = secondsToEnd
        this.secondsSecondaryBeforeEnd = secondsSecondaryBeforeEnd

        if (isTimerStarted.not()) {
            startTimer()
        } else {
            resetTimer(secondsToEnd, secondsSecondaryBeforeEnd)
        }
    }

    public fun resumeTimer() {
        startTimer(secondsToEnd, secondsSecondaryBeforeEnd)
    }

    public fun pauseTimer() {
        timer.cancel()
        timer.purge()

        val secondsToEnd = this.secondsLeft
        val secondsSecondaryBeforeEnd = this.secondsSecondaryBeforeEnd
        resetCounters()

        this.secondsToEnd = secondsToEnd
        this.secondsSecondaryBeforeEnd = secondsSecondaryBeforeEnd

        isTimerStarted = false
    }

    public fun stopTimer() {
        timer.cancel()
        timer.purge()
        timerInteraction.onMainTimerFinished()
        resetCounters()
    }

    public fun resetTimer(secondsToEnd: Long, secondsSecondaryBeforeEnd: Long) {
        stopTimer()
        startTimer(secondsToEnd, secondsSecondaryBeforeEnd)
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
                if (secondsLeft == secondsSecondaryBeforeEnd) {
                    timerInteraction.onSecondaryTimerFinished()
                }
                timerInteraction.onNewTimerCycle(secondsLeft)
            }
        }, TIMER_DELAY, TIMER_INTERVAL)
    }

    private fun resetCounters() {
        secondsToEnd = 0
        secondsElapsed = 0
        secondsSecondaryBeforeEnd = 0
        isTimerStarted = false
    }
}

interface ATimerInteraction {
    fun onMainTimerFinished()

    fun onSecondaryTimerFinished()

    fun onNewTimerCycle(timeLeft: Long)
}