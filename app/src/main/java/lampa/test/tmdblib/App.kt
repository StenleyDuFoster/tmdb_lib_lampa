package lampa.test.tmdblib

import android.app.Application
import android.content.Context
import lampa.test.tmdblib.Koin.appComponent
import org.koin.android.ext.android.startKoin
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class App : Application() {

    companion object: KoinComponent {
        val contextComponent: Context by inject()
    }

    override fun onCreate() {
        super.onCreate()
        initCoin()
    }

    private fun initCoin() {
        startKoin(
            androidContext = this,
            modules = appComponent
        )
    }
}