package lampa.test.tmdblib.model.viewmodel.repository.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import lampa.test.tmdblib.model.viewmodel.repository.local.dao.LoggedInUserDao
import lampa.test.tmdblib.model.viewmodel.repository.local.enity.LoggedInUser

@Database(entities = (arrayOf(LoggedInUser::class)), version = 4, exportSchema = false)
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
                                .fallbackToDestructiveMigration()
                                .build()

                fun destroyInstance() {

                        if (INSTANCE?.isOpen == true) {
                                INSTANCE?.close()
                        }

                        INSTANCE = null
                }
        }
}