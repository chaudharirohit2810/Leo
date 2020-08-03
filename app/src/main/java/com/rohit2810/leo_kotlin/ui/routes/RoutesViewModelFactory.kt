package com.rohit2810.leo_kotlin.ui.routes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rohit2810.leo_kotlin.ui.map.MapsViewModel
import java.lang.IllegalArgumentException

class RoutesViewModelFactory(var context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RouteViewModel::class.java)) {
            return RouteViewModel(context) as T
        }
        throw IllegalArgumentException("Not assignable to map view model")
    }

}