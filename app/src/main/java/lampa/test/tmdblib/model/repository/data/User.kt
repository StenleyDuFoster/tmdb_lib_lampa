package lampa.test.tmdblib.model.repository.data

import lampa.test.tmdblib.model.repository.local.enity.LoggedInUser

data class User (

    val name: String,
    val pass: String,
    val token: String,
    val signIn: Boolean,
    var session: String
){
    fun toDatabaseFormat(): LoggedInUser{

        val loggedInUser = LoggedInUser(
            name,
            pass,
            token,
            signIn,
            session
        )
        return loggedInUser
    }
}