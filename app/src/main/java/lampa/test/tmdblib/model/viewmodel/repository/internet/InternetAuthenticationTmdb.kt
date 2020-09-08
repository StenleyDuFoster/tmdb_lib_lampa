package lampa.test.tmdblib.model.viewmodel.repository.internet

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import lampa.test.tmdblib.contract_interface.CallBackFromInternetAuthToLoginViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.model.viewmodel.repository.internet.api.JsonTmdbPlaceHolderApi
import lampa.test.tmdblib.util.constant.ApiConstant
import org.koin.core.KoinComponent
import org.koin.core.inject

class InternetAuthenticationTmdb (val callBackFromInternetAuthToLoginViewModel: CallBackFromInternetAuthToLoginViewModel) :
    MainContract.InternetAuth, KoinComponent {

    private val compositeDisposable = CompositeDisposable()

    override fun startAuth(){

        val jsonPlaceHolderApi: JsonTmdbPlaceHolderApi by inject()
        compositeDisposable.add(
        jsonPlaceHolderApi.getSession(ApiConstant().API_V3)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { responseData ->
                    callBackFromInternetAuthToLoginViewModel.onAuthenticationTmdbSuccess(responseData.guest_session_id!!)
                }, { error("Auth callBack failure") })
        )

    }

    override fun dispose() {
        if(!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}