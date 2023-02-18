package com.example.recyclersample.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PersonFave::class], version = 1)
abstract class PersonFaveDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: PersonFaveDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): PersonFaveDatabase {
            if (INSTANCE == null) {
                synchronized(PersonFaveDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        PersonFaveDatabase::class.java, "person_fave_database").build()
                }
            }
            return INSTANCE as PersonFaveDatabase
        }
    }

    abstract fun personFaveDao() : PersonFaveDao
}