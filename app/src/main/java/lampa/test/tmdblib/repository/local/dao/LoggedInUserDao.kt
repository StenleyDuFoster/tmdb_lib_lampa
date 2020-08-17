package lampa.test.tmdblib.repository.local.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import lampa.test.tmdblib.repository.local.enity.LoggedInUser

@Dao
open interface LoggedInUserDao {

      @Query("SELECT * FROM login_user_table")
      fun getAll(): Flowable<List<LoggedInUser?>?>

      @Insert
      fun insert(loggedInUser: LoggedInUser): Completable

      @Update
      fun update(loggedInUser: LoggedInUser): Completable

      @Query("DELETE FROM login_user_table")//rx
      fun delete(): Completable
}