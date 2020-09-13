package lampa.test.tmdblib.model.viewmodel.repository.data

import lampa.test.tmdblib.model.viewmodel.repository.local.enity.LoggedInUser

data class UserData (

    val login: String,
    val password: String,
    val signInMethod: Int,
    val token: String,
    val session_id: String
){
    fun toDatabaseFormat(): LoggedInUser {

        return LoggedInUser(
            login,
            password,
            signInMethod,
            token,
            session_id
        )
    }
}