package lampa.test.tmdblib.repository.data

import android.net.wifi.hotspot2.pps.Credential
import com.google.firebase.auth.AuthCredential
import lampa.test.tmdblib.repository.local.enity.LoggedInUser

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