package anoda.mobi.anoda_turn_timer.di

import anoda.mobi.anoda_turn_timer.ui.settings.SettingsPresenter
import anoda.mobi.anoda_turn_timer.ui.timer.TimerPresenter
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class])
interface ApplicationComponent {

    fun inject(presenter: TimerPresenter)
    fun inject(presenter: SettingsPresenter)

}