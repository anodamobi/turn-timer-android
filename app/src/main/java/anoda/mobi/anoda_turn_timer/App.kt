package anoda.mobi.anoda_turn_timer

import android.app.Application
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import timber.log.Timber

class App : Application() {
    companion object {
        var typeFaceRancho: Typeface? = null
    }

    override fun onCreate() {
        super.onCreate()

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

}