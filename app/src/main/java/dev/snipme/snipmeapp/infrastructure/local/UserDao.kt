package dev.snipme.snipmeapp.infrastructure.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun register(user: UserEntry): Completable

    @Query("SELECT id FROM users WHERE email = :email AND password = :password")
    fun login(email: String, password: String): Single<Int>

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    fun identify(email: String): Single<Int>

    @Query("SELECT * FROM users WHERE id = :id")
    fun user(id: Int) : Single<UserEntry>

}