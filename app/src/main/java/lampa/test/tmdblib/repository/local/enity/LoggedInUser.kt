package lampa.test.tmdblib.repository.local.enity

import android.net.wifi.hotspot2.pps.Credential
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.auth.AuthCredential

@Entity(tableName = "login_user_table")
class LoggedInUser(

    @PrimaryKey
    val login: String,
    val password: String,
    val signInMethod: Int,
    val token: String,
    val session_id: String
)