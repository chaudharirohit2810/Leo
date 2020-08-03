package com.rohit2810.leo_kotlin.ui.routes

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rohit2810.leo_kotlin.repository.HeatmapsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class RouteViewModel(var context: Context): ViewModel() {
    var job = Job()
    var coroutinScoope = CoroutineScope(job + Dispatchers.Main)
    private var repository = HeatmapsRepository.getInstance(context)

    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean>
        get() = _isDialogVisible

    private val _isProgressBarVisible = MutableLiveData<Boolean>()
    val isProgressBarVisible: LiveData<Boolean>
        get() = _isProgressBarVisible



    init {
        _isDialogVisible.value = true
        _isProgressBarVisible.value = false
    }

    fun setProgressBar() {
        _isProgressBarVisible.value = true
    }

    fun disableProgressBar() {
        _isProgressBarVisible.value = false
    }

    fun getDirections( lat1: Double, long1: Double, lat2: Double, long2: Double) {
        coroutinScoope.launch {
            _isProgressBarVisible.value = true
            repository.getDirections(lat1, long1, lat2, long2)
            Timber.d("Executed")
            disableProgressBar()
        }
    }

    fun disableDialog() {
        _isDialogVisible.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}