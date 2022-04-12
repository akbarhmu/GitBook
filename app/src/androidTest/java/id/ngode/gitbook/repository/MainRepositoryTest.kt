package id.ngode.gitbook.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import id.ngode.gitbook.database.FavoriteUserDao
import id.ngode.gitbook.database.FavoriteUserRoomDatabase
import id.ngode.gitbook.model.UserResponse
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4ClassRunner::class)
class MainRepositoryTest: TestCase() {
    private lateinit var favoriteUserDao: FavoriteUserDao
    private lateinit var db: FavoriteUserRoomDatabase
    private var user: UserResponse = UserResponse()


    @Before
    public override fun setUp(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, FavoriteUserRoomDatabase::class.java
        ).build()
        favoriteUserDao = db.favoriteUserDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun insertUser() {
        user.apply {
            login = "hmminatu"
            avatarUrl = "https://avatars.githubusercontent.com/u/68300628?v=4"
            htmlUrl = "https://github.com/hmminatu"
        }
        favoriteUserDao.insert(user)
        val users = favoriteUserDao.getAllFavoriteUsers()
        LiveDataTestUtil.getValue(users)?.let { assertTrue(it.contains(user)) }
    }

    object LiveDataTestUtil {
        /**
         * Get the value from a LiveData object. We're waiting for LiveData to emit, for 2 seconds.
         * Once we got a notification via onChanged, we stop observing.
         */
        private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

        @Throws(InterruptedException::class)
        fun <T> getValue(liveData: LiveData<T>): T? {
            val data = arrayOfNulls<Any>(1)
            val latch = CountDownLatch(1)
            val observer: Observer<T?> = object : Observer<T?> {
                override fun onChanged(o: T?) {
                    data[0] = o
                    latch.countDown()
                    liveData.removeObserver(this)
                }
            }
            executorService.execute { observer }
            latch.await(2, TimeUnit.SECONDS)
            @Suppress("UNCHECKED_CAST")
            return data[0] as T?
        }
    }
}