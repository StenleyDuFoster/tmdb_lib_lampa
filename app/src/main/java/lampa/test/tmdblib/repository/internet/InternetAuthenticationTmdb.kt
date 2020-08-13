package lampa.test.tmdblib.repository.internet

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import lampa.test.tmdblib.contract_interface.CallBackFromInternetAuthToLoginViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.dagger.component.DaggerComponent
import lampa.test.tmdblib.repository.internet.api.JsonPlaceHolderApi

class InternetAuthenticationTmdb (val callBackFromInternetAuthToLoginViewModel: CallBackFromInternetAuthToLoginViewModel)
    : MainContract.InternetAuth {

    val jsonPlaceHolderApi: JsonPlaceHolderApi

    init {

        jsonPlaceHolderApi = DaggerComponent.create().getTmdbPlaceHolderApi()
        createSession()
    }

    override fun createSession() {

        jsonPlaceHolderApi.getSession("9bb79091064ef827e213e1b974a3b718")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { responseData ->

                    callBackFromInternetAuthToLoginViewModel.onAuthenticationTmdbSuccess(responseData.guest_session_id!!)
                },
                { })
    }
}