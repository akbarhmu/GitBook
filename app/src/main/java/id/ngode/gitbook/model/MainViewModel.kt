package id.ngode.gitbook.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ngode.gitbook.api.ApiConfig
import id.ngode.gitbook.helper.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listSearchUser = MutableLiveData<List<UserResponse?>?>()
    val listSearchUser: LiveData<List<UserResponse?>?> = _listSearchUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    init {
        getUser()
    }

    private fun getUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers()
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listSearchUser.value = response.body()?.items
                } else {
                    _toastText.value = Event("Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("Failed: ${t.message}")
            }
        })
    }

    fun getSearchUser(username: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserSearch(username)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listSearchUser.value = response.body()?.items
                } else {
                    _toastText.value = Event("Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                _toastText.value = Event("Failed: ${t.message}")
            }
        })
    }
}