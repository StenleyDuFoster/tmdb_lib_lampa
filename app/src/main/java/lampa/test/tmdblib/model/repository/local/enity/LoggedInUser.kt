package lampa.test.tmdblib.model.repository.local.enity

import androidx.room.Entity
import androidx.room.PrimaryKey

    @Entity(tableName = "login_user_table")
    class LoggedInUser(

        @PrimaryKey
        val login: String,
        val password: String,
        val token: String
    )