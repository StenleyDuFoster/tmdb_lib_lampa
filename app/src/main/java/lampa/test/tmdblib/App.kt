package lampa.test.tmdblib

import android.app.Application
import lampa.test.tmdblib.dagger.component.ContextComponent
import lampa.test.tmdblib.dagger.component.DaggerContextComponent
import lampa.test.tmdblib.dagger.module.ContextModule

class App : Application() {
    companion object {
        lateinit var applicationComponent: ContextComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        applicationComponent = DaggerContextComponent.builder()
            .contextModule(ContextModule(this))
            .build()
    }
}