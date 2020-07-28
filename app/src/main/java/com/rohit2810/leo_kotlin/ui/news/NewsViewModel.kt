package com.rohit2810.leo_kotlin.ui.news

import android.content.Context
import androidx.lifecycle.ViewModel
import com.rohit2810.leo_kotlin.models.news.News
import com.rohit2810.leo_kotlin.repository.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewsViewModel(var context: Context): ViewModel() {
    private var viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private var newsRepository = NewsRepository.getInstance(context)
    var city: String = "pune"

    fun insertNews() {
        coroutineScope.launch {
            newsRepository.insertNews(city)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}