package lampa.test.tmdblib.model.repository.local.enity

import androidx.room.Entity
import androidx.room.PrimaryKey

    @Entity(tableName = "word_table")
    class LoggedInUser(

        @PrimaryKey
        val login: String,
        val password: String
    )