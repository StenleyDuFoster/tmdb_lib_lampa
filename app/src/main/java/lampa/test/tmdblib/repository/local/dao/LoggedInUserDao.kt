package lampa.test.tmdblib.repository.local.dao

import androidx.room.*
import io.reactivex.Flowable
import lampa.test.tmdblib.repository.local.enity.LoggedInUser

@Dao
open interface LoggedInUserDao {

      @Query("SELECT * FROM login_user_table")
      fun getAll(): Flowable<List<LoggedInUser?>?>

      @Insert
      fun insert(loggedInUser: LoggedInUser)

      @Update
      fun update(loggedInUser: LoggedInUser)//rx

      @Query("DELETE FROM login_user_table")//rx
      fun delete()
}