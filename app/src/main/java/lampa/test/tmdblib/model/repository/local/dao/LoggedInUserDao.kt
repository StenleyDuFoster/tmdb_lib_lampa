package lampa.test.tmdblib.model.repository.local.dao

import androidx.room.*
import lampa.test.tmdblib.model.repository.local.enity.LoggedInUser

    @Dao
    open interface LoggedInUserDao {
//        @Query("SELECT * FROM loggedInUser")
//        fun getAll(): List<LoggedInUser?>?

        @Insert
        fun insert(loggedInUser: LoggedInUser?)

        @Update
        fun update(loggedInUser: LoggedInUser?)

        @Delete
        fun delete(loggedInUser: LoggedInUser?)
    }