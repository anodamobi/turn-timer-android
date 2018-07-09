package anoda.mobi.anoda_turn_timer

import android.app.Application
import anoda.mobi.anoda_turn_timer.di.AppModule
import anoda.mobi.anoda_turn_timer.di.ApplicationComponent
import anoda.mobi.anoda_turn_timer.di.DaggerApplicationComponent

class App : Application() {

    companion object {
        lateinit var appComponent: ApplicationComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = buildAppComponent()

    }

    private fun buildAppComponent(): ApplicationComponent {
        return DaggerApplicationComponent.builder().appModule(AppModule(this)).build()
    }
}