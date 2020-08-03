package com.rohit2810.leo_kotlin.ui.feedback

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FeedbackViewModel(var context: Context): ViewModel() {
    var category = MutableLiveData<String>()
    var description = MutableLiveData<String>()
    var filename = MutableLiveData<String>()

    private val _onSubmit = MutableLiveData<Boolean>()
    val onSubmit: LiveData<Boolean>
        get() = _onSubmit

    private val _fileUpload = MutableLiveData<Boolean>()
    val fileUpload: LiveData<Boolean>
        get() = _fileUpload

    private val _isFileSelected = MutableLiveData<Boolean>()
    val isFileSelected: LiveData<Boolean>
        get() = _isFileSelected

    init {
        _isFileSelected.value = false
    }

    fun fileSelected() {
        _isFileSelected.value = true
    }

    fun doneFileSelection() {
        _isFileSelected.value = false
    }

    fun onFileUpload() {
        _fileUpload.value = true
    }

    fun doneFileUpload() {
        _fileUpload.value = null
    }

    fun onSubmit() {
        _onSubmit.value = true
    }

    fun doneOnSubmit() {
        _onSubmit.value = null
    }
}