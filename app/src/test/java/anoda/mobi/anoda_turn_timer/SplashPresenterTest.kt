package anoda.mobi.anoda_turn_timer

import anoda.mobi.anoda_turn_timer.ui.splash.SplashPresenter
import anoda.mobi.anoda_turn_timer.ui.splash.SplashView
import anoda.mobi.anoda_turn_timer.ui.splash.`SplashView$$State`
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(SplashPresenter::class)
class SplashPresenterTest {

    @Test
    fun testStartAnimationCalledOnStart() {
        val splashView: SplashView = Mockito.mock(SplashView::class.java)
        val splashViewState = Mockito.mock(`SplashView$$State`::class.java)
        val presenter = SplashPresenter()
        presenter.setViewState(splashViewState)
        presenter.attachView(splashView)

        Mockito.verify(splashViewState).startAnimation()
        presenter.onDestroy()
    }

    @Test(timeout = 3000)
    fun testStartTimerActivityCalledOnStart() {
        val splashView: SplashView = Mockito.mock(SplashView::class.java)
        val splashViewState = Mockito.mock(`SplashView$$State`::class.java)
        val presenter = SplashPresenter()
        presenter.setViewState(splashViewState)
        presenter.attachView(splashView)

        runBlocking(UI) {
            Mockito.verify(splashViewState).startTimerActivity()
        }
        presenter.onDestroy()
    }

    @Test
    fun testPresenterStartAnimationCalled() {
        val splashView: SplashView = Mockito.mock(SplashView::class.java)
        val splashViewState = Mockito.mock(`SplashView$$State`::class.java)
        val presenter = PowerMockito.spy(SplashPresenter())
        presenter.setViewState(splashViewState)
        presenter.attachView(splashView)

        PowerMockito.verifyPrivate(presenter).invoke("startAnimation")
        presenter.onDestroy()
    }

    @Test
    fun testPresenterStartTimeAndStartNextActivityCalled() {
        val splashView: SplashView = Mockito.mock(SplashView::class.java)
        val splashViewState = Mockito.mock(`SplashView$$State`::class.java)
        val presenter = PowerMockito.spy(SplashPresenter())
        presenter.setViewState(splashViewState)
        presenter.attachView(splashView)

        PowerMockito.verifyPrivate(presenter).invoke("startTimeAndStartNextActivity")
        presenter.onDestroy()
    }
}