package anoda.mobi.anoda_turn_timer

import android.app.Application
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import anoda.mobi.anoda_turn_timer.di.AppModule
import anoda.mobi.anoda_turn_timer.di.ApplicationComponent
import anoda.mobi.anoda_turn_timer.di.DaggerApplicationComponent
import timber.log.Timber
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric

class App : Application() {
    companion object {
        var typeFaceRancho: Typeface? = null

        lateinit var appComponent: ApplicationComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()
        initializeFabric()

        appComponent = buildAppComponent()

        if (BuildConfig.DEBUG) {
            initTimber()
        }

        initializeTypeFaces()

    }

    private fun initializeTypeFaces() {
        typeFaceRancho = ResourcesCompat.getFont(this, R.font.rancho_regular)
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun buildAppComponent(): ApplicationComponent {
        return DaggerApplicationComponent.builder().appModule(AppModule(this)).build()
    }

    private fun initializeFabric() {
        val crashCore = CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build()
        val crashlyticBuilder = Crashlytics.Builder()
                .core(crashCore)
                .build()
        val fabricBuilder = Fabric.Builder(this)
                .kits(crashlyticBuilder)
                .build()
        Fabric.with(fabricBuilder)
    }

}