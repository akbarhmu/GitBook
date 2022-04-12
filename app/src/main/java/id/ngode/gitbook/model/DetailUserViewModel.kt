package id.ngode.gitbook.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.ngode.gitbook.api.ApiConfig
import id.ngode.gitbook.helper.Event
import id.ngode.gitbook.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(private val username: String, application: Application) : ViewModel() {

    private val mMainRepository: MainRepository = MainRepository(application)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listUserFollowers = MutableLiveData<List<UserResponse>>()
    val listUserFollowers: LiveData<List<UserResponse>> = _listUserFollowers

    private val _listUserFollowing = MutableLiveData<List<UserResponse>>()
    val listUserFollowing: LiveData<List<UserResponse>> = _listUserFollowing

    private val _detailUser = MutableLiveData<UserResponse?>()
    val detailUser: LiveData<UserResponse?> = _detailUser

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    init {
        getDetailUser()
    }

    private fun getDetailUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detailUser.value = responseBody
                    }
                } else {
                    _toastText.value = Event("Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("Failed: ${t.message}")
            }
        })
    }

    fun getUserFollowers() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(call: Call<List<UserResponse>>, response: Response<List<UserResponse>>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _listUserFollowers.value = response.body()
                } else {
                    _toastText.value = Event("Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("Failed: ${t.message}")
            }
        })
    }

    fun getUserFollowing() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(call: Call<List<UserResponse>>, response: Response<List<UserResponse>>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _listUserFollowing.value = response.body()
                } else {
                    _isLoading.value = false
                    _toastText.value = Event("Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("Failed: ${t.message}")
            }
        })
    }

    fun addFavoriteUser(userResponse: UserResponse) {
        mMainRepository.insert(userResponse)
    }

    fun deleteFavoriteUser(userResponse: UserResponse?) {
        if (userResponse != null) {
            mMainRepository.delete(userResponse)
        }
    }

    fun countUser(username: String): LiveData<Int> {
        return mMainRepository.countUser(username)
    }

    class Factory(val app: String?, private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailUserViewModel(app.toString(), application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}