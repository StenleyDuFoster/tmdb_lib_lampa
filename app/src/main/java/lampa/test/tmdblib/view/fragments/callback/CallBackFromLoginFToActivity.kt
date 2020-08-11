package lampa.test.tmdblib.view.fragments.callback

import lampa.test.tmdblib.repository.data.UserData

interface CallBackFromLoginFToActivity {

    fun userLogin(userData: UserData)
}