package anoda.mobi.anoda_turn_timer

import android.content.Context
import anoda.mobi.anoda_turn_timer.ui.timer.TimerPresenter
import anoda.mobi.anoda_turn_timer.ui.timer.TimerView
import anoda.mobi.anoda_turn_timer.ui.timer.`TimerView$$State`
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class TimerPresenterTest {

    @Mock
    lateinit var timerView: TimerView

    @Mock
    lateinit var timerViewState: `TimerView$$State`

    lateinit var presenter: TimerPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val context = Mockito.mock(Context::class.java)
        presenter = Mockito.mock(TimerPresenter::class.java)
        presenter.context = context
        presenter.setViewState(timerViewState)
        presenter.attachView(timerView)
    }

    @After
    fun tearDown() {
        presenter.onDestroy()
    }

    @Test(timeout = 2000)
    fun updateTextOnStart() {
        runBlocking(UI) {
            Mockito.verify(timerViewState).updateTimerText("")
        }
    }


}