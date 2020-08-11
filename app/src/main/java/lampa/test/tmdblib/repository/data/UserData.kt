package lampa.test.tmdblib.repository.data

import lampa.test.tmdblib.repository.local.enity.LoggedInUser

data class UserData (

    val name: String,
    val pass: String,
    val token: String,
    val signIn: Boolean,
    var session: String
){
    fun toDatabaseFormat(): LoggedInUser {

        return LoggedInUser(
            name,
            pass,
            token,
            signIn,
            session
        )
    }
}