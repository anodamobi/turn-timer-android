package anoda.mobi.anoda_turn_timer.di

import anoda.mobi.anoda_turn_timer.ui.main.MainPresenter
import anoda.mobi.anoda_turn_timer.ui.settings.SettingsPresenter
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class])
interface ApplicationComponent {

    fun inject(presenter: MainPresenter)
    fun inject(presenter: SettingsPresenter)

}