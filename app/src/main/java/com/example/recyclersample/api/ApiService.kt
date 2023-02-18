package com.example.recyclersample.api

import retrofit2.Call
import retrofit2.http.*

/* ENDPOINT
   * Search : https://api.github.com/search/users?q={username}
  Detail user : https://api.github.com/users/{username}
  List Follower : https://api.github.com/users/{username}/followers
  List Following : https://api.github.com/users/{username}/following
   */

interface ApiService {
    @GET("users")
    fun getUserList() : Call<List<UserDetail>>

    @GET("users/{user}")
    fun getUserDetail(
        @Path("user") user: String
    ): Call<UserDetail>

    @GET("users/{user}/followers")
    fun getUserFollowers(
        @Path("user") user: String
    ): Call<List<UserDetail>>

    @GET("users/{user}/following")
    fun getUserFollowing(
        @Path("user") user: String
    ): Call<List<UserDetail>>

    @GET("search/users")
    fun getUserSearch(
        @Query("q") q:String
    ): Call<UserSearch>
}