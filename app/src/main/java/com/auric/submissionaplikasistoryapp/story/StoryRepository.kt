package com.auric.submissionaplikasistoryapp.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.auric.submissionaplikasistoryapp.Api.UserModel
import com.auric.submissionaplikasistoryapp.Api.UserPreference
import com.auric.submissionaplikasistoryapp.StoryAppApi
import com.auric.submissionaplikasistoryapp.StoryAppRetrofitInstance
import com.auric.submissionaplikasistoryapp.model.ListStoryItem
import com.auric.submissionaplikasistoryapp.model.LoginResult
import com.auric.submissionaplikasistoryapp.model.StoriesResponse

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StoryRepository constructor(
 //   private val storyDatabase: StoryDatabase,
    private val apiService: StoryAppApi,
    private val preference: UserPreference
) {

    private val _userLogin = MutableLiveData<LoginResult>()
    val userLogin: LiveData<LoginResult> = _userLogin

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStoryWithLocation(token: String?) {
        apiService.getAllStoriesLocation("Bearer $token")
                .enqueue(object: Callback<StoriesResponse> {
                    override fun onResponse(
                        call: Call<StoriesResponse>,
                        response: Response<StoriesResponse>
                    ) {
                        if(response.isSuccessful) {
                            _listStory.postValue(response.body()?.listStory)
                        }
                    }

                    override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                        Log.e("FETCHINGSTORIES", t.message.toString())
                    }
                })
        }
    }
