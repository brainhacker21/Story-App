package com.auric.submissionaplikasistoryapp.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.auric.submissionaplikasistoryapp.model.ListStoryItem
import com.auric.submissionaplikasistoryapp.story.StoryRepository


class MapsViewModel  constructor (private val repository: StoryRepository) : ViewModel() {

    val listStory: LiveData<List<ListStoryItem>> = repository.listStory

    fun getAllStoryWithMaps(token: String?) {
        repository.getStoryWithLocation(token)
    }
}