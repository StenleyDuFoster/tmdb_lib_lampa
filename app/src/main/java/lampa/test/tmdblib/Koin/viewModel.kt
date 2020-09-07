package lampa.test.tmdblib.Koin

import lampa.test.tmdblib.model.viewmodel.VideoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModel = module {
    viewModel { VideoViewModel() }
}