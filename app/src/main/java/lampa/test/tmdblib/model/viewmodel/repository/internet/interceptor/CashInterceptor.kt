package lampa.test.tmdblib.model.viewmodel.repository.internet.interceptor

import lampa.test.tmdblib.util.connection_manager.NetworkChangeReceiver
import okhttp3.Interceptor
import okhttp3.Response

class CashInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        request = if (NetworkChangeReceiver.networkState) {
            request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
        } else {
            request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                .build()
        }
        return chain.proceed(request)
    }
}