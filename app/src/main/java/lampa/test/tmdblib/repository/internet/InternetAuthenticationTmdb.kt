package lampa.test.tmdblib.repository.internet

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import lampa.test.tmdblib.contract_interface.CallBackFromInternetAuthToLoginViewModel
import lampa.test.tmdblib.contract_interface.MainContract
import lampa.test.tmdblib.dagger.component.DaggerRetrofitComponent
import lampa.test.tmdblib.util.constant.ApiConstant

class InternetAuthenticationTmdb (val callBackFromInternetAuthToLoginViewModel: CallBackFromInternetAuthToLoginViewModel) :
    MainContract.InternetAuth {

    override fun startAuth(){

        val jsonPlaceHolderApi = DaggerRetrofitComponent.create().getTmdbPlaceHolderApiByRetrofit()
        jsonPlaceHolderApi.getSession(ApiConstant().API_V3)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { responseData ->
                    callBackFromInternetAuthToLoginViewModel.onAuthenticationTmdbSuccess(responseData.guest_session_id!!)
                }, { error("Auth callBack failure") })

    }
}