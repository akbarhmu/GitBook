package id.ngode.gitbook.database

import androidx.lifecycle.LiveData
import androidx.room.*
import id.ngode.gitbook.model.UserResponse

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(userResponse: UserResponse) : Long

    @Update
    fun update(userResponse: UserResponse)

    @Delete
    fun delete(userResponse: UserResponse)

    @Query("SELECT * from tb_favorites ORDER BY id ASC")
    fun getAllFavoriteUsers(): LiveData<List<UserResponse>>

    @Query("SELECT COUNT() from tb_favorites where login = :username")
    fun countUser(username: String): LiveData<Int>
}