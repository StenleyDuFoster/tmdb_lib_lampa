package lampa.test.tmdblib.fragments.callback

import lampa.test.tmdblib.model.repository.data.User

interface CallBackFromLoginFToActivity {

    fun userLogin(user: User)
}