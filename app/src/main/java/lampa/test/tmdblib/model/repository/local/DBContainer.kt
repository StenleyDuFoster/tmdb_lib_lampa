package lampa.test.tmdblib.model.repository.local

import android.app.Application
import androidx.room.Room
import lampa.test.tmdblib.model.repository.local.database.LoggedDatabase


//class DBContainer: Application() {
//
//    var instances: DBContainer = this
//    private lateinit var database: LoggedDatabase
//
//    override fun onCreate() {
//        super.onCreate()
//        database = Room.databaseBuilder(applicationContext, LoggedDatabase::class.java, "database")
//            .build()
//    }
//
//    open fun getInstance(): DBContainer? {
//        return instances
//    }
//
//    open fun getDatabase(): LoggedDatabase? {
//        return database
//    }
//
//}