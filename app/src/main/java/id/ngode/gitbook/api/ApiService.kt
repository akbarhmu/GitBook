package id.ngode.gitbook.api

import id.ngode.gitbook.model.SearchResponse
import id.ngode.gitbook.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users?q=akbar%20hamaminatu")
    fun getUsers() : Call<SearchResponse>

    @GET("search/users?")
    fun getUserSearch(
        @Query("q") username: String?
    ): Call<SearchResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String?
    ) : Call<UserResponse>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String?
    ) : Call<List<UserResponse>>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String?
    ) : Call<List<UserResponse>>
}