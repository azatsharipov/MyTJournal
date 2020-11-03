package com.example.mytjournal.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytjournal.data.model.User
import com.example.mytjournal.data.repository.TJournalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    var user: User? = null

    fun auth(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            user = TJournalRepository().auth(token)
        }
    }
}