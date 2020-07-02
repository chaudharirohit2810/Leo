package com.example.leo_kotlin.ui.emergencyContacts

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.leo_kotlin.models.User
import com.example.leo_kotlin.repository.TroubleRepository
import java.lang.IllegalArgumentException

class EmergencyContactsViewModelFactory(
    val user: User,
    private val repository: TroubleRepository, val context: Context,
    private val toUpdate: Boolean
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmergencyContactsViewModel::class.java)) {
            return EmergencyContactsViewModel(
                previousUser = user,
                repository = repository,
                context = context,
                toUpdate = toUpdate
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}