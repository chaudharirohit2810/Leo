package com.rohit2810.leo_kotlin.ui.emergencyContacts

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohit2810.leo_kotlin.models.user.User
import com.rohit2810.leo_kotlin.repository.TroubleRepository
import com.rohit2810.leo_kotlin.utils.getEmergencyContactFromCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class EmergencyContactsViewModel(
    private val previousUser: User,
    private val repository: TroubleRepository,
    private val context: Context,
    private val toUpdate: Boolean
) : ViewModel() {

    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _isProgressBarVisible = MutableLiveData<Boolean>()
    val isProgressBarVisible: LiveData<Boolean>
        get() = _isProgressBarVisible


    val phone1 = MutableLiveData<String>()
    val phone2 = MutableLiveData<String>()
    val phone3 = MutableLiveData<String>()
    val phone4 = MutableLiveData<String?>()
    val phone5 = MutableLiveData<String?>()


    init {
        _isProgressBarVisible.value = false
        if (toUpdate) {
            initList()
        }

    }

    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean>
        get() = _navigateToMain

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String>
        get() = _toastMsg

    private val _snackbarMsg = MutableLiveData<String>()
    val snackbarMsg: LiveData<String>
        get() = _snackbarMsg


    // Register new user and then navigate to main fragment
    fun addUserAndNavigateToMain() {
        coroutineScope.launch {
            _isProgressBarVisible.value = true
            try {
                    checkNumbersFilledCondition()
                    checkNumberLengthCondition()
                    checkNumberDecimalCondition()
                    checkUniquePhoneNumbers()
                    val newUser = previousUser
                    val list = arrayOf<String?>(phone1.value, phone2.value, phone3.value, phone4.value, phone5.value)
                    newUser.emergencyContacts = list.toMutableList()

                    // To update is to check if we are entered in activity to update the user or to user initial user
                    if (toUpdate) {
                        repository.updateUser(newUser)
                    } else {
                        repository.registerUser(newUser)
                    }


                    if(toUpdate) {
                        _snackbarMsg.value = "Emergency contacts updated"
                    }else {
                        _snackbarMsg.value = "Welcome ${newUser.name}"
                    }
                    _isProgressBarVisible.value = false

                    _navigateToMain.value = true

            } catch (e: Exception) {
                _isProgressBarVisible.value = false
                Timber.d(e.localizedMessage)
                _snackbarMsg.value = e.localizedMessage
            }

        }
    }

    private fun checkNumberLengthCondition() {
        if(!phone1.value.isNullOrEmpty() && phone1.value!!.trim().length != 10) {
            throw Exception("Phone number length should be 10")

        }
        if(!phone2.value.isNullOrEmpty() && phone2.value!!.trim().length != 10) {
            throw Exception("Phone number length should be 10")

        }
        if(!phone3.value.isNullOrEmpty() && phone3.value!!.trim().length != 10) {
            throw Exception("Phone number length should be 10")

        }
        if(!phone4.value.isNullOrEmpty() && phone4.value!!.trim().length != 10) {
            throw Exception("Phone number length should be 10")
        }
        if(!phone5.value.isNullOrEmpty() && phone5.value!!.trim().length != 10) {
            throw Exception("Phone number length should be 10")
        }
    }

    private fun checkNumbersFilledCondition() {
        var count = 0
        if(phone1.value.isNullOrEmpty()) count++
        if(phone2.value.isNullOrEmpty()) count++
        if (phone3.value.isNullOrEmpty()) count++
        if (phone4.value.isNullOrEmpty()) count++
        if (phone5.value.isNullOrEmpty()) count++
        if(count >= 3) throw  Exception("Please Enter at least 3 emergency contacts")
    }

    private fun checkNumberDecimalCondition() {
        checkNumberDecimalConditionUtil(phone1.value)
        checkNumberDecimalConditionUtil(phone2.value)
        checkNumberDecimalConditionUtil(phone3.value)
        checkNumberDecimalConditionUtil(phone4.value)
        checkNumberDecimalConditionUtil(phone5.value)

    }

    private fun checkNumberDecimalConditionUtil( s : String?) {
        if (!s.isNullOrEmpty() && !Regex("^[0-9]*$").matches(s)) {
            throw Exception("Phone number should only contain numbers")
        }
    }

    private fun checkUniquePhoneNumbers() {
        var count = 0
        var hashset = hashSetOf<String>()
        if(!phone1.value.isNullOrEmpty()) {
            Timber.d("Entered phone1")
            hashset.add(phone1.value!!)
            count++
        }
        if(!phone2.value.isNullOrEmpty()) {
            Timber.d("Entered phone2")
            hashset.add(phone2.value!!)
            count++
        }
        if(!phone3.value.isNullOrEmpty()) {
            Timber.d("Entered phone3")
            hashset.add(phone3.value!!)
            count++
        }
        if(!phone4.value.isNullOrEmpty()) {
            Timber.d("Entered phone4")
            hashset.add(phone4.value!!)
            count++
        }
        if(!phone5.value.isNullOrEmpty()) {
            Timber.d("Entered phone5")
            hashset.add(phone5.value!!)
            count++
        }
        Timber.d("Count: ${count} Hashset Size: ${hashset.size}")
        if(count !== hashset.size) {
            throw Exception("Please enter unique phone numbers")
        }
    }

    private fun initList() {
        val list = getEmergencyContactFromCache(context)
        list.let {
            list[0]?.let {
                phone1.value = it
            }
            list[1]?.let {
                phone2.value = it
            }
            list[2]?.let {
                phone3.value = it
            }
            list[3]?.let {
                phone4.value = it
            }
            list[4]?.let {
                phone5.value = it
            }

        }
    }


    // After the toast message is shown
    fun doneShowingToastMsg() {
        _toastMsg.value = null
    }

    //After the snackbar is shown
    fun doneShowingSnackbar() {
        _snackbarMsg.value = null
    }

    fun doneNavigateToMain() {
        _navigateToMain.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}