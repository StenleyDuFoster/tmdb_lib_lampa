package lampa.test.tmdblib.model.viewmodel.repository.local.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import lampa.test.tmdblib.model.viewmodel.repository.local.enity.LoggedInUser

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