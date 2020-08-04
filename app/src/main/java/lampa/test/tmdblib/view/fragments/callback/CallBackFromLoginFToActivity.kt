package lampa.test.tmdblib.fragments.callback

import lampa.test.tmdblib.model.repository.data.UserData

interface CallBackFromLoginFToActivity {

    fun userLogin(userData: UserData)
}