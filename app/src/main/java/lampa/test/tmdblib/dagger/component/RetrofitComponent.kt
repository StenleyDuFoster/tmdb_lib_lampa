package lampa.test.tmdblib.dagger.component

import dagger.Component
import lampa.test.tmdblib.dagger.module.RetrofitTmdbModule
import lampa.test.tmdblib.repository.internet.api.JsonTmdbPlaceHolderApi
import javax.inject.Singleton

@Singleton
@Component (modules = [RetrofitTmdbModule::class])
interface RetrofitComponent {

    fun getTmdbPlaceHolderApiByRetrofit() : JsonTmdbPlaceHolderApi
}