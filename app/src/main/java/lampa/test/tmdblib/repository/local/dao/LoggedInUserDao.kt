package lampa.test.tmdblib.repository.local.dao

import androidx.room.*
import lampa.test.tmdblib.repository.local.enity.LoggedInUser

    @Dao
    open interface LoggedInUserDao {
        @Query("SELECT * FROM login_user_table")
        fun getAll(): List<LoggedInUser?>?

        @Insert
        fun insert(loggedInUser: LoggedInUser?)

        @Update
        fun update(loggedInUser: LoggedInUser?)

        @Delete
        fun delete(loggedInUser: LoggedInUser?)
    }