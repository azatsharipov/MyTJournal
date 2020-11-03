package com.example.mytjournal.ui.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytjournal.data.model.Post
import com.example.mytjournal.data.repository.TJournalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedViewModel : ViewModel() {
    val posts = MutableLiveData<MutableList<Post>>()
    val count = 5
    var offset = 0

    fun loadPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val gettedPosts = TJournalRepository().getPosts(count, offset)
            if (gettedPosts != null)
                posts.postValue(gettedPosts)
            offset += count
        }
    }

}