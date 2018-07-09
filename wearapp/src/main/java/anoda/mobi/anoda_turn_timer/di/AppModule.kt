package anoda.mobi.anoda_turn_timer.di

import android.content.Context
import anoda.mobi.anoda_turn_timer.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {

    @Provides
    @Singleton
    fun getContext(): Context = app

}