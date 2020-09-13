package lampa.test.tmdblib

import android.app.Application
import android.content.Context
import lampa.test.tmdblib.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin

class App : Application() {

    companion object: KoinComponent {
        lateinit var contextComponent: Context
    }

    override fun onCreate() {
        super.onCreate()
        contextComponent = applicationContext
        initCoin()
    }

    private fun initCoin() {
        startKoin {
            androidContext(applicationContext)
            modules(appComponent)
        }
    }
}