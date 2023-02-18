package com.example.recyclersample.data

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PersonFaveSource (application: Application) {
    private val mPersonFaveDao: PersonFaveDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = PersonFaveDatabase.getDatabase(application)
        mPersonFaveDao = db.personFaveDao()
    }

    fun getAllPersonFaves() :
            LiveData<List<PersonFave>> = mPersonFaveDao.getAllPersonFaves()
    fun getPersonFaveByUsername(username: String) :
            LiveData<Boolean> = mPersonFaveDao.findPersonFaveByUsername(username)

    fun insert(personFave: PersonFave) {
        executorService.execute { mPersonFaveDao.insert(personFave)
        }
    }
    fun delete(personFave: PersonFave) {
        executorService.execute { mPersonFaveDao.delete(personFave)
        }
    }
}