package anoda.mobi.anoda_turn_timer

import android.content.Context
import anoda.mobi.anoda_turn_timer.ui.settings.SettingsPresenter
import anoda.mobi.anoda_turn_timer.ui.settings.SettingsView
import anoda.mobi.anoda_turn_timer.ui.settings.`SettingsView$$State`
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(SettingsPresenter::class)
class SettingsPresenterTest {

    @Test
    fun onGettingActualTimeCalledTest() {
        val settingsView = Mockito.mock(SettingsView::class.java)
        val settingsViewState = Mockito.mock(`SettingsView$$State`::class.java)
        val presenter = PowerMockito.mock(SettingsPresenter::class.java)
        val context = Mockito.mock(Context::class.java)
        presenter.context = context
        presenter.setViewState(settingsViewState)

        PowerMockito.`when`(presenter.attachView(settingsView)).thenCallRealMethod()
        presenter.attachView(settingsView)

        PowerMockito.verifyPrivate(presenter).invoke("onGettingActualTime")

        presenter.onDestroy()
    }

    @Test
    fun onSaveTimeToPrefsCalledTest() {
        val settingsView = Mockito.mock(SettingsView::class.java)
        val settingsViewState = Mockito.mock(`SettingsView$$State`::class.java)
        val presenter = PowerMockito.mock(SettingsPresenter::class.java)
        val context = Mockito.mock(Context::class.java)
        presenter.context = context
        presenter.setViewState(settingsViewState)
        presenter.attachView(settingsView)

        PowerMockito.`when`(presenter.detachView(settingsView)).thenCallRealMethod()
        presenter.detachView(settingsView)

        PowerMockito.verifyPrivate(presenter).invoke("saveTimeToPrefs")

        presenter.onDestroy()
    }

    @Test
    fun onChangeBeepTimeCalledTest() {
        val settingsView = Mockito.mock(SettingsView::class.java)
        val settingsViewState = Mockito.mock(`SettingsView$$State`::class.java)
        val presenter = PowerMockito.mock(SettingsPresenter::class.java)
        val context = Mockito.mock(Context::class.java)
        presenter.context = context
        presenter.setViewState(settingsViewState)
        presenter.attachView(settingsView)

        val minutes = 10

        PowerMockito.`when`(presenter.onChangeBeepTimeMinutes(minutes)).thenCallRealMethod()
        presenter.onChangeBeepTimeMinutes(minutes)

        PowerMockito.verifyPrivate(presenter).invoke("setBeforeBeepTime")

        presenter.onDestroy()
    }

    @Test
    fun onChangeRoundDurataionCalledTest() {
        val settingsView = Mockito.mock(SettingsView::class.java)
        val settingsViewState = Mockito.mock(`SettingsView$$State`::class.java)
        val presenter = PowerMockito.mock(SettingsPresenter::class.java)
        val context = Mockito.mock(Context::class.java)
        presenter.context = context
        presenter.setViewState(settingsViewState)
        presenter.attachView(settingsView)

        val minutes = 10

        PowerMockito.`when`(presenter.onChangeRoundDurationTimeMinutes(minutes)).thenCallRealMethod()
        presenter.onChangeRoundDurationTimeMinutes(minutes)

        PowerMockito.verifyPrivate(presenter).invoke("setRoundDurationTime")

        presenter.onDestroy()
    }
}