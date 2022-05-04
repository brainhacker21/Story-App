package com.auric.submissionaplikasistoryapp.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.auric.submissionaplikasistoryapp.Api.UserPreference
import com.auric.submissionaplikasistoryapp.signin.SigninViewModel

class StoryViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SigninViewModel::class.java) -> {
                SigninViewModel(pref) as T
            }

            modelClass.isAssignableFrom(StoryPreferenceModel::class.java) -> {
                StoryPreferenceModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}