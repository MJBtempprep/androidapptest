package com.example.recyclersample.mainPage

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recyclersample.detailPage.PersonDetailViewModel
import com.example.recyclersample.favePage.PersonFaveViewModel
import com.example.recyclersample.prefer.PreferenceSetting

class PersonListViewModelFactory (private val mApplication: Application, private val mUsername: String, private val pref: PreferenceSetting) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonListViewModel::class.java)) {
            return PersonListViewModel(pref) as T
        } else if (modelClass.isAssignableFrom(PersonFaveViewModel::class.java)) {
            return PersonFaveViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(PersonDetailViewModel::class.java)) {
            return PersonDetailViewModel(mApplication, mUsername) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}