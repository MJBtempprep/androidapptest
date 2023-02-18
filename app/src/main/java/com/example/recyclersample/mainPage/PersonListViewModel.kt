package com.example.recyclersample.mainPage

import android.util.Log
import androidx.lifecycle.*
import com.example.recyclersample.api.ApiConfig
import com.example.recyclersample.api.UserDetail
import com.example.recyclersample.api.UserSearch
import com.example.recyclersample.prefer.PreferenceSetting
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonListViewModel(private val pref: PreferenceSetting): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    private val _personList=MutableLiveData<List<UserDetail>>()
    private val _isError = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean> = _isLoading
    val personList: LiveData<List<UserDetail>> =_personList
    val isError : LiveData<Boolean> = _isError
    val isDarkMode : LiveData<Boolean> = pref.getThemeSetting().asLiveData()

    fun doneToastErrorInput() { _isError.value = false }
    fun checkDarkMode() : Boolean? { return isDarkMode.value }
    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    init {
        displayUserList()
    }

    fun displayUserList() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserList()
        client.enqueue(object : Callback<List<UserDetail>> {
            override fun onResponse(call: Call<List<UserDetail>>, response: Response<List<UserDetail>>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _personList.value = response.body()
                }
            }
            override fun onFailure(call: Call<List<UserDetail>>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }

    fun setUserList(username:String){
        //_isLoading.value = true
        val clientHunter = ApiConfig.getApiService().getUserSearch(username)
        clientHunter.enqueue(object : Callback<UserSearch> {
            override fun onResponse(
                call: Call<UserSearch>,
                response: Response<UserSearch>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null ) {
                    _personList.postValue(responseBody.items)
                }
            }
            override fun onFailure(call: Call<UserSearch>, t: Throwable) {
                Log.e("ur dead", "onFailure: ${t.message}")
            } })
    }

    fun getUserList() : LiveData<List<UserDetail>> {
        return _personList
    }



}