package lampa.test.tmdblib.dagger.component

import android.content.Context
import dagger.Component
import lampa.test.tmdblib.dagger.module.ContextModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class])
interface ContextComponent {

    fun getContext():Context
}