package lampa.test.tmdblib

import android.app.Application
import android.content.Context
import android.widget.Toast
import lampa.test.tmdblib.dagger.component.ContextComponent
import lampa.test.tmdblib.dagger.component.DaggerContextComponent
import lampa.test.tmdblib.dagger.module.ContextModule

class App : Application() {
    companion object {
        lateinit var contextComponent: ContextComponent
    }

    override fun onCreate() {
        super.onCreate()
        createContextWithDagger()
    }

    private fun createContextWithDagger() {
        contextComponent = DaggerContextComponent.builder()
            .contextModule(ContextModule(this))
            .build()
    }


}