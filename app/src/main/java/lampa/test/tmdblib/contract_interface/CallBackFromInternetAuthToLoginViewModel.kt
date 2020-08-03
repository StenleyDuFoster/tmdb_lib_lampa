package lampa.test.tmdblib.contract_interface

interface CallBackFromInternetAuthToLoginViewModel {

    fun onAuthenticationTmdbSuccess(session_id: String)
}