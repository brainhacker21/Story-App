package com.auric.submissionaplikasistoryapp.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.auric.submissionaplikasistoryapp.StoryAppRetrofitInstance
import com.auric.submissionaplikasistoryapp.model.ListStoryItem
import com.auric.submissionaplikasistoryapp.model.StoriesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel: ViewModel() {

    private var _listStoryItem = MutableLiveData<List<ListStoryItem>>()

    fun getListStoryItem(token: String) {
        StoryAppRetrofitInstance.apiService()
            .getAllListStories("Bearer $token")
            .enqueue(object: Callback<StoriesResponse> {
                override fun onResponse(
                    call: Call<StoriesResponse>,
                    response: Response<StoriesResponse>
                ) {
                    if(response.isSuccessful) {
                        _listStoryItem.postValue(response.body()?.listStory)
                    }
                }

                override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                    Log.e("FETCHINGSTORIES", t.message.toString())
                }
            })
    }

    fun getListStoryItem(): LiveData<List<ListStoryItem>> {
        return _listStoryItem
    }
}