package id.ngode.gitbook.repository

import android.app.Application
import androidx.lifecycle.LiveData
import id.ngode.gitbook.database.FavoriteUserDao
import id.ngode.gitbook.database.FavoriteUserRoomDatabase
import id.ngode.gitbook.model.UserResponse
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    fun getAllFavoriteUsers(): LiveData<List<UserResponse>> = mFavoriteUserDao.getAllFavoriteUsers()

    fun insert(userResponse: UserResponse) {
        executorService.execute { mFavoriteUserDao.insert(userResponse) }
    }

    fun delete(userResponse: UserResponse) {
        executorService.execute { mFavoriteUserDao.delete(userResponse) }
    }

    fun countUser(username: String): LiveData<Int> {
        return mFavoriteUserDao.countUser(username)
    }
}