package com.example.recyclersample.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class UserSearch(
    @field:SerializedName("items")
    val items:List<UserDetail>
)

@Parcelize
data class UserDetail(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("location")
    val location: String?,

    @field:SerializedName("public_repos")
    val publicRepos: String?,

    @field:SerializedName("company")
    val company: String?,

    @field:SerializedName("followers")
    val followers: String?,

    @field:SerializedName("following")
    val following: String?,

    @field:SerializedName("followers_url")
    val followersUrl: String?,

    @field:SerializedName("following_url")
    val followingUrl: String?,

    @field:SerializedName("avatar_url")
    val avatarUrl: String?
): Parcelable




