package lampa.test.tmdblib.model.viewmodel.repository.local.enity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "login_user_table")
class LoggedInUser(

    @PrimaryKey
    val login: String,
    val password: String,
    val signInMethod: Int,
    val token: String,
    val session_id: String
)