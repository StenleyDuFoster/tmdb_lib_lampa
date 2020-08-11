package lampa.test.tmdblib.repository.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import lampa.test.tmdblib.repository.local.dao.LoggedInUserDao
import lampa.test.tmdblib.repository.local.enity.LoggedInUser

@Database(entities = (arrayOf(LoggedInUser::class)), version = 2, exportSchema = false)
abstract class LoggedDatabase: RoomDatabase() {
        abstract fun loggedInUserDao(): LoggedInUserDao
    }