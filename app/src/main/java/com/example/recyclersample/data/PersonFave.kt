package com.example.recyclersample.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class PersonFave(
    @PrimaryKey(autoGenerate = false)

    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String? = null,

    @ColumnInfo(name = "login")
    var login: String? = null,

    @ColumnInfo(name = "type")
    var type: String? = null
) : Parcelable