package id.ngode.gitbook.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.ngode.gitbook.repository.MainRepository

class FavoriteViewModel(application: Application) : ViewModel() {

    private val mMainRepository: MainRepository = MainRepository(application)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getFavoriteUser()
    }

    fun getFavoriteUser(): LiveData<List<UserResponse>> {
        _isLoading.value = true
        val list = mMainRepository.getAllFavoriteUsers()
        _isLoading.value = false
        return list
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FavoriteViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}