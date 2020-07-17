package com.rohit2810.leo_kotlin.ui.register

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohit2810.leo_kotlin.models.User
import com.rohit2810.leo_kotlin.repository.TroubleRepository
import com.rohit2810.leo_kotlin.utils.sha256
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class RegisterViewModel(var context: Context) : ViewModel() {
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val phone = MutableLiveData<String>()
    val name = MutableLiveData<String>()

    private val _isProgressBarVisible = MutableLiveData<Boolean>()
    val isProgressBarVisible: LiveData<Boolean>
        get() = _isProgressBarVisible

    init {
        _isProgressBarVisible.value = false
    }

    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val repository = TroubleRepository.getInstance(context)

    private val _registeredUser = MutableLiveData<User>()
    val registeredUser: LiveData<User>
        get() = _registeredUser

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String>
        get() = _toastMsg

    fun navigateToEmergencyNumbers() {
        val condition = !username.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
                && !phone.value.isNullOrEmpty() && !name.value.isNullOrEmpty()
                && !confirmPassword.value.isNullOrEmpty() && !email.value.isNullOrEmpty()
        if (condition) {
            if (password.value == confirmPassword.value) {
                if (password.value?.length!! >= 8) {
                    if (phone.value?.trim()?.length == 10) {
                        coroutineScope.launch {
                            try {
                                checkNumberDecimalConditionUtil(phone.value)
                                checkEmail(email.value)
                                _isProgressBarVisible.value = true
                                val tempUser =
                                    username.value?.let {
                                        password.value?.let { it1 ->
                                            User(
                                                username = it,
                                                password = it1,
                                                phone = phone.value,
                                                name = name.value,
                                                email = email.value
                                            )
                                        }
                                    }
                                repository.checkUsername(tempUser!!)
                                repository.sendOtp(tempUser!!)
                                _registeredUser.value = tempUser
                                _isProgressBarVisible.value = false
                            } catch (e: Exception) {
                                _toastMsg.value = e.localizedMessage
                                _isProgressBarVisible.value = false
                            }
                        }
                    } else {
                        _toastMsg.value = "Phone number length should be 10"
                    }
                } else {
                    _toastMsg.value = "Password length should be greater or equal to 8"
                }
            } else {
                _toastMsg.value = "Passwords do not match"
            }
        } else {
            _toastMsg.value = "Please fill all the fields"
        }
    }


    private fun checkNumberDecimalConditionUtil(s: String?) {
        if (!s.isNullOrEmpty() && !Regex("^[0-9]*$").matches(s)) {
            throw Exception("Phone number should only contain numbers")
        }
    }

    private fun checkEmail(s: String?) {
        if (!s.isNullOrEmpty() && !Regex("[^@]+@[^\\.]+\\..+").matches(s)) {
            throw Exception("Invalid email address")
        }
    }

    fun doneShowingToastMsg() {
        _toastMsg.value = null
    }

    fun doneNavigateToEmergencyNumbers() {
        _registeredUser.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}