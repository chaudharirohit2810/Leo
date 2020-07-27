package com.rohit2810.leo_kotlin.ui.map

import android.content.Context
import androidx.lifecycle.ViewModel
import com.rohit2810.leo_kotlin.repository.HeatmapsRepository
import com.rohit2810.leo_kotlin.utils.getLatitudeFromCache
import com.rohit2810.leo_kotlin.utils.getLongitudeFromCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MapsViewModel(var context: Context) : ViewModel() {
    private var viewModelJob = Job()
    private var coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var repository = HeatmapsRepository.getInstance(context)

    init {
        insertHeatmaps()
    }

    fun insertHeatmaps() {
        coroutineScope.launch {
            repository.insertHeatmaps(
                latitude = getLatitudeFromCache(context).toDouble(),
                longitude = getLongitudeFromCache(context).toDouble()
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}