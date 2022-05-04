package com.auric.submissionaplikasistoryapp

import com.auric.submissionaplikasistoryapp.Api.FileUploadResponse
import com.auric.submissionaplikasistoryapp.model.StoriesResponse
import com.auric.submissionaplikasistoryapp.model.UserLoginResponse
import com.auric.submissionaplikasistoryapp.model.UserRegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface StoryAppApi {

    @FormUrlEncoded
    @POST("login")
    fun getLoginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserLoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun createAccount(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserRegisterResponse>

    @GET("stories")
    fun getAllListStories(
        @Header("Authorization") auth: String
    ): Call<StoriesResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<FileUploadResponse>

    @GET("stories")
    fun getAllStoriesLocation(
        @Header("Authorization") auth: String,
        @Query("location")location: Int = 1

    ): Call<StoriesResponse>
}