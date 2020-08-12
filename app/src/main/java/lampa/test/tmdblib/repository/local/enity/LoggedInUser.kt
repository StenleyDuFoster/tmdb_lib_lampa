package lampa.test.tmdblib.repository.local.enity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "login_user_table")
class LoggedInUser(

    @PrimaryKey
    val login: String,
    val password: String,
    val token: String,
    val signIn: Boolean,
    val session_id: String
)