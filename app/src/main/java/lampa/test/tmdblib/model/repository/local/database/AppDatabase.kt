package lampa.test.tmdblib.model.repository.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import lampa.test.tmdblib.model.repository.local.dao.LoggedInUserDao
import lampa.test.tmdblib.model.repository.local.enity.LoggedInUser

@Database(entities = (arrayOf(LoggedInUser::class)), version = 1)
abstract class AppDatabase: RoomDatabase() {
        abstract fun loggedInUserDao(): LoggedInUserDao
    }