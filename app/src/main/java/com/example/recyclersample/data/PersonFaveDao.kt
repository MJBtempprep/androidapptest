package com.example.recyclersample.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PersonFaveDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(personFave: PersonFave)

    @Query("SELECT * FROM personFave ORDER BY id ASC")
    fun getAllPersonFaves() : LiveData<List<PersonFave>>

    @Query("SELECT EXISTS(SELECT * FROM personFave WHERE login = :username)")
    fun findPersonFaveByUsername(username: String) : LiveData<Boolean>

    @Update
    fun update(personFave: PersonFave)

    @Delete
    fun delete(personFave: PersonFave)
}