package anoda.mobi.anoda_turn_timer

import android.content.Context
import anoda.mobi.anoda_turn_timer.ui.timer.TimerPresenter
import anoda.mobi.anoda_turn_timer.ui.timer.TimerView
import anoda.mobi.anoda_turn_timer.ui.timer.`TimerView$$State`
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox

@RunWith(PowerMockRunner::class)
@PrepareForTest(TimerPresenter::class)
class TimerPresenterTest {

    @Test(timeout = 2000)
    fun updateTextOnStartTest() {
        val timerView = Mockito.mock(TimerView::class.java)
        val timerViewState = Mockito.mock(`TimerView$$State`::class.java)
        val presenter = Mockito.mock(TimerPresenter::class.java)
        val context = Mockito.mock(Context::class.java)
        presenter.context = context
        presenter.setViewState(timerViewState)
        presenter.attachView(timerView)

        runBlocking(UI) {
            Mockito.verify(timerViewState).updateTimerText("")
        }

        presenter.onDestroy()
    }

    @Test
    fun onStartTimerClickAndTimerNotPausedTest() {
        val timerView = Mockito.mock(TimerView::class.java)
        val timerViewState = Mockito.mock(`TimerView$$State`::class.java)
        val presenter = PowerMockito.mock(TimerPresenter::class.java)
        val context = Mockito.mock(Context::class.java)
        presenter.context = context
        presenter.setViewState(timerViewState)
        presenter.attachView(timerView)

        Whitebox.setInternalState(presenter, "isTimerPaused", false)

        PowerMockito.`when`(presenter.onStartTimerClick()).thenCallRealMethod()
        presenter.onStartTimerClick()

        PowerMockito.verifyPrivate(presenter).invoke("startTimer")

        presenter.onDestroy()
    }

    @Test
    fun onStartTimerClickAndTimerPausedTest() {
        val timerView = Mockito.mock(TimerView::class.java)
        val timerViewState = Mockito.mock(`TimerView$$State`::class.java)
        val presenter = PowerMockito.mock(TimerPresenter::class.java)
        val context = Mockito.mock(Context::class.java)
        presenter.context = context
        presenter.setViewState(timerViewState)
        presenter.attachView(timerView)

        Whitebox.setInternalState(presenter, "isTimerPaused", true)

        PowerMockito.`when`(presenter.onStartTimerClick()).thenCallRealMethod()
        presenter.onStartTimerClick()

        PowerMockito.verifyPrivate(presenter).invoke("resumeTimer")

        presenter.onDestroy()
    }

    @Test
    fun onPauseTimerClickTest() {
        val timerView = Mockito.mock(TimerView::class.java)
        val timerViewState = Mockito.mock(`TimerView$$State`::class.java)
        val presenter = PowerMockito.mock(TimerPresenter::class.java)
        val context = Mockito.mock(Context::class.java)
        presenter.context = context
        presenter.setViewState(timerViewState)
        presenter.attachView(timerView)

        PowerMockito.`when`(presenter.onPauseTimerClick()).thenCallRealMethod()
        presenter.onPauseTimerClick()

        PowerMockito.verifyPrivate(presenter).invoke("pauseTimer")

        presenter.onDestroy()
    }

    @Test
    fun onResetTimerClickTest() {
        val timerView = Mockito.mock(TimerView::class.java)
        val timerViewState = Mockito.mock(`TimerView$$State`::class.java)
        val presenter = PowerMockito.mock(TimerPresenter::class.java)
        val context = Mockito.mock(Context::class.java)
        presenter.context = context
        presenter.setViewState(timerViewState)
        presenter.attachView(timerView)

        PowerMockito.`when`(presenter.onResetTimerClick()).thenCallRealMethod()
        presenter.onResetTimerClick()

        PowerMockito.verifyPrivate(presenter).invoke("resetTimer")

        presenter.onDestroy()
    }

    @Test
    fun onTimerTextClickAndTimerStartedTest() {
        val timerView = Mockito.mock(TimerView::class.java)
        val timerViewState = Mockito.mock(`TimerView$$State`::class.java)
        val presenter = PowerMockito.mock(TimerPresenter::class.java)
        val context = Mockito.mock(Context::class.java)
        presenter.context = context
        presenter.setViewState(timerViewState)
        presenter.attachView(timerView)

        Whitebox.setInternalState(presenter, "isTimerStarted", false)

        PowerMockito.`when`(presenter.onTimerTextClick()).thenCallRealMethod()
        presenter.onTimerTextClick()

        PowerMockito.verifyPrivate(presenter).invoke("startTimer")

        presenter.onDestroy()
    }

    @Test
    fun onTimerTextClickAndTimerNotStartedTest() {
        val timerView = Mockito.mock(TimerView::class.java)
        val timerViewState = Mockito.mock(`TimerView$$State`::class.java)
        val presenter = PowerMockito.mock(TimerPresenter::class.java)
        val context = Mockito.mock(Context::class.java)
        presenter.context = context
        presenter.setViewState(timerViewState)
        presenter.attachView(timerView)

        Whitebox.setInternalState(presenter, "isTimerStarted", true)

        PowerMockito.`when`(presenter.onTimerTextClick()).thenCallRealMethod()
        presenter.onTimerTextClick()

        PowerMockito.verifyPrivate(presenter).invoke("resetTimer")

        presenter.onDestroy()
    }

    @Test
    fun onSettingsClickTest() {
        val timerView = Mockito.mock(TimerView::class.java)
        val timerViewState = Mockito.mock(`TimerView$$State`::class.java)
        val presenter = PowerMockito.mock(TimerPresenter::class.java)
        val context = Mockito.mock(Context::class.java)
        presenter.context = context
        presenter.setViewState(timerViewState)
        presenter.attachView(timerView)

        Mockito.`when`(presenter.viewState).thenReturn(timerViewState)
        PowerMockito.`when`(presenter.onSettingsClick()).thenCallRealMethod()
        presenter.onSettingsClick()

        Mockito.verify(timerViewState).startSettingsActivity()

        presenter.onDestroy()
    }

    @Test
    fun onMainTimerFinishedTest() {
        val timerView = Mockito.mock(TimerView::class.java)
        val timerViewState = Mockito.mock(`TimerView$$State`::class.java)
        val presenter = PowerMockito.mock(TimerPresenter::class.java)
        val context = Mockito.mock(Context::class.java)
        presenter.context = context
        presenter.setViewState(timerViewState)
        presenter.attachView(timerView)

        Mockito.`when`(presenter.viewState).thenReturn(timerViewState)
        PowerMockito.`when`(presenter.onMainTimerFinished()).thenCallRealMethod()
        presenter.onMainTimerFinished()

        Mockito.verify(timerViewState).showTimerEndProgress()
        Mockito.verify(timerViewState).showStartButton()

        presenter.onDestroy()
    }

    @Test
    fun onNewTimerCycleTest() {
        val timerView = Mockito.mock(TimerView::class.java)
        val timerViewState = Mockito.mock(`TimerView$$State`::class.java)
        val presenter = PowerMockito.mock(TimerPresenter::class.java)
        val context = Mockito.mock(Context::class.java)
        presenter.context = context
        presenter.setViewState(timerViewState)
        presenter.attachView(timerView)

        val timeleft = 70L
        val timeleftFormatted = "1:10"

        Mockito.`when`(presenter.viewState).thenReturn(timerViewState)
        PowerMockito.`when`(presenter.onNewTimerCycle(timeleft)).thenCallRealMethod()
        presenter.onNewTimerCycle(timeleft)

        PowerMockito.verifyPrivate(presenter).invoke("formatText", timeleft)

        presenter.onDestroy()
    }

    @Test
    fun onSecondaryTimerFinishedTest() {
        val timerView = Mockito.mock(TimerView::class.java)
        val timerViewState = Mockito.mock(`TimerView$$State`::class.java)
        val presenter = PowerMockito.mock(TimerPresenter::class.java)
        val context = Mockito.mock(Context::class.java)
        presenter.context = context
        presenter.setViewState(timerViewState)
        presenter.attachView(timerView)

        Mockito.`when`(presenter.viewState).thenReturn(timerViewState)
        PowerMockito.`when`(presenter.onSecondaryTimerFinished()).thenCallRealMethod()
        presenter.onSecondaryTimerFinished()

        Mockito.verify(timerViewState).playSignal()

        presenter.onDestroy()
    }
}