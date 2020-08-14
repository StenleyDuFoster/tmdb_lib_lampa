package lampa.test.tmdblib.dagger.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule(val contextApp: Context) {

    @Provides
    @Singleton
    fun getContext(): Context {

        return contextApp
    }
}