package lampa.test.tmdblib.repository.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import lampa.test.tmdblib.repository.local.dao.LoggedInUserDao
import lampa.test.tmdblib.repository.local.enity.LoggedInUser

@Database(entities = (arrayOf(LoggedInUser::class)), version = 2, exportSchema = false)
abstract class LoggedDatabase: RoomDatabase() {
        abstract fun loggedInUserDao(): LoggedInUserDao

        companion object {

                @Volatile private var INSTANCE: LoggedDatabase? = null

                fun getInstance(context: Context): LoggedDatabase =
                        INSTANCE ?: synchronized(this) {
                                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                        }

                private fun buildDatabase(context: Context) =
                        Room.databaseBuilder(context,
                                LoggedDatabase::class.java, "Sample.db")
                                .build()
        }
}