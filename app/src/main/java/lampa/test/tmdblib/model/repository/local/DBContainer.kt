package lampa.test.tmdblib.model.repository.local

import android.app.Application
import androidx.room.Room
import lampa.test.tmdblib.model.repository.local.database.AppDatabase


class DBContainer: Application() {

    var instances: DBContainer = this
    lateinit private var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, AppDatabase::class.java, "database")
            .build()
    }

    fun getInstance(): DBContainer? {
        return instances
    }

    open fun getDatabase(): AppDatabase? {
        return database
    }

}