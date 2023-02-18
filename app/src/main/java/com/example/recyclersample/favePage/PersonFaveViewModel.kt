package com.example.recyclersample.favePage

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.recyclersample.data.PersonFave
import com.example.recyclersample.data.PersonFaveSource

class PersonFaveViewModel(application: Application) : ViewModel() {
    private val mPersonFaveSource : PersonFaveSource = PersonFaveSource(application)
    val faveList : LiveData<List<PersonFave>> = mPersonFaveSource.getAllPersonFaves()
}