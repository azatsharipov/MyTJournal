package com.example.mytjournal.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytjournal.data.model.User
import com.example.mytjournal.data.repository.TJournalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    val user = MutableLiveData<User?>()

    fun auth(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = TJournalRepository().auth(token)
                if (result.isSuccessful && result.body()?.result != null) {
                    user.postValue(result.body()?.result)
                } else {
                    // error
                    user.postValue(null)
                }
            } catch (e: Exception) {
//                user.postValue(User(e.toString(), ""))
                user.postValue(null)
            }
        }
    }
}