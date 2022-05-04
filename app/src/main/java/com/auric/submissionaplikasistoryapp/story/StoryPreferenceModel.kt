package com.auric.submissionaplikasistoryapp.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.auric.submissionaplikasistoryapp.Api.UserModel
import com.auric.submissionaplikasistoryapp.Api.UserPreference
import kotlinx.coroutines.launch

class StoryPreferenceModel (private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<UserModel?> {
        return pref.getUser().asLiveData()
    }
}