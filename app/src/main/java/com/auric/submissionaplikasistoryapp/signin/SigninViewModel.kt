package com.auric.submissionaplikasistoryapp.signin

import android.util.Log
import androidx.lifecycle.*
import com.auric.submissionaplikasistoryapp.Api.UserModel
import com.auric.submissionaplikasistoryapp.Api.UserPreference
import com.auric.submissionaplikasistoryapp.StoryAppApi
import com.auric.submissionaplikasistoryapp.StoryAppRetrofitInstance
import com.auric.submissionaplikasistoryapp.model.LoginResult
import com.auric.submissionaplikasistoryapp.model.UserLoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<UserModel?> {
        return pref.getUser().asLiveData()
    }
    }