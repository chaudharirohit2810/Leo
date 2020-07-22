package com.rohit2810.leo_kotlin.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohit2810.leo_kotlin.repository.TroubleRepository
import com.rohit2810.leo_kotlin.utils.getIsInTrouble
import com.rohit2810.leo_kotlin.utils.getUserFromCache
import kotlinx.coroutines.*
import timber.log.Timber

class MainViewModel(
    private val context: Context
) : ViewModel() {

    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val repository = TroubleRepository.getInstance(context)


    // Util for navigating to maps
    private val _navigateToMaps = MutableLiveData<Boolean>()
    val navigateToMaps: LiveData<Boolean>
        get() = _navigateToMaps


    private val _snackbarMsg = MutableLiveData<String>()
    val snackbarMsg: LiveData<String>
        get() = _snackbarMsg

    private val _isProgressBarVisible = MutableLiveData<Boolean>()
    val isProgressBarVisible: LiveData<Boolean>
        get() = _isProgressBarVisible

    private val _showNotInTrouble = MutableLiveData<Boolean>()
    val showNotInTrouble: LiveData<Boolean>
        get() = _showNotInTrouble

    private val _undoTrouble = MutableLiveData<Boolean>()
    val undoTrouble: LiveData<Boolean>
        get() = _undoTrouble

    private val _navigateToNews = MutableLiveData<Boolean>()
    val navigateToNews: LiveData<Boolean>
        get() = _navigateToNews

    private val _isNotInTroubleVisible = MutableLiveData<Boolean>()
    val isNotInTroubleVisible: LiveData<Boolean>
        get() = _isNotInTroubleVisible

    private val _connectToP2P = MutableLiveData<Boolean>()
    val connectToP2P: LiveData<Boolean>
        get() = _connectToP2P

    init {
        _isProgressBarVisible.value = false
        _showNotInTrouble.value = false
        _undoTrouble.value = false
        _isNotInTroubleVisible.value = false
        _showNotInTrouble.value = getIsInTrouble(context)
    }


    // To navigate to map
    fun navigateToMaps() {
        _navigateToMaps.value = true
    }

    // After navigated to map
    fun doneNavigateToMaps() {
        _navigateToMaps.value = null
    }


    private val _sendTrouble = MutableLiveData<Boolean>()
    val sendTrouble: LiveData<Boolean>
        get() = _sendTrouble

    private val _showPrecautionsDialog = MutableLiveData<Boolean>()
    val showPrecautionsDialog: LiveData<Boolean>
        get() = _showPrecautionsDialog

    fun sendTrouble() {
        _sendTrouble.value = true
    }

    fun doneSendTrouble() {
        _sendTrouble.value = null
    }

    private val _unMarkTrouble = MutableLiveData<Boolean>()
    val unMarkTrouble: LiveData<Boolean>
        get() = _unMarkTrouble

    fun unMarkTroubleUtil() {
        _unMarkTrouble.value = true
        _undoTrouble.value = true
    }

    fun doneUnMarkTrouble() {
        _unMarkTrouble.value = null
    }

    private val _toastMsg = MutableLiveData<String>()
    val toastMsg: LiveData<String>
        get() = _toastMsg


    fun markTrouble() {

        coroutineScope.launch {
            try {
                _undoTrouble.value = false
                _isProgressBarVisible.value = true
                _toastMsg.value =
                    "You can click on I am not in trouble button, if you mistakenly pressed the button"
                _isNotInTroubleVisible.value = true
                delay(5 * 1000)
                _isNotInTroubleVisible.value = false
                if (!_undoTrouble.value!!) {
                    Timber.d("Marking in trouble")
                    val user = getUserFromCache(context) ?: throw Exception("Some error occured")
                    repository.markTrouble(user)
                    delay(3 * 1000)
                    _isProgressBarVisible.value = false
                    _toastMsg.value = "You've marked yourself in trouble. Help is on the way!!"
                }else {
                    throw Exception("Trouble request canceled")
                }
            } catch (e: Exception) {
                _isNotInTroubleVisible.value = false
                _isProgressBarVisible.value = false
                _toastMsg.value = e.localizedMessage
            }

        }
    }

    fun doneConnectToP2P() {
        _connectToP2P.value = null
    }

    fun unMarkTrouble() {
        coroutineScope.launch {
            try {
                val user = getUserFromCache(context) ?: throw Exception("Some error occured")
                repository.unMarkTrouble(user)
                _showNotInTrouble.value = false
                _toastMsg.value = "Trouble unmarked!!"
            } catch (e: Exception) {
                _toastMsg.value = e.localizedMessage
            }
        }
    }

    fun navigateToNews() {
        _navigateToNews.value = true
    }

    fun doneNavigateToNews() {
        _navigateToNews.value = null
    }

    fun showPrecautionsDialog() {
        _showPrecautionsDialog.value = true
    }

    fun doneShowPrecautionsDialog() {
        _showPrecautionsDialog.value = null
    }


    fun doneShowingToastMsg() {
        _toastMsg.value = null
    }

    fun doneShowingSnackbarMsg() {
        _snackbarMsg.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}