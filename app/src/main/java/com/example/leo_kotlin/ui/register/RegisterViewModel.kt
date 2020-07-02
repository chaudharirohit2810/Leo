package com.example.leo_kotlin.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.leo_kotlin.models.User

class RegisterViewModel: ViewModel() {
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()
    val phone = MutableLiveData<String>()
    val name = MutableLiveData<String>()

    private val _registeredUser = MutableLiveData<User>()
    val registeredUser : LiveData<User>
        get() = _registeredUser

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg : LiveData<String>
        get() = _toastMsg

    fun navigateToEmergencyNumbers() {
        val condition = !username.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
                && !phone.value.isNullOrEmpty() && !name.value.isNullOrEmpty() && !confirmPassword.value.isNullOrEmpty()
        if(condition) {
            if(password.value == confirmPassword.value) {
                _registeredUser.value =
                    username.value?.let {
                        password.value?.let{ it1 ->
                            User(username = it, password = it1, phone = phone.value, name = name.value)
                        }
                    }
            }else{
                _toastMsg.value = "Passwords do not match"
            }
        }else {
            _toastMsg.value = "Please fill all the fields"
        }
    }

    fun doneShowingToastMsg() {
        _toastMsg.value = null
    }

    fun doneNavigateToEmergencyNumbers() {
        _registeredUser.value = null
    }
}