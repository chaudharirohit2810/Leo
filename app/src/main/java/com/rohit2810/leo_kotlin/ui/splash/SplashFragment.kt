package com.rohit2810.leo_kotlin.ui.splash

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rohit2810.leo_kotlin.Application
import com.rohit2810.leo_kotlin.R
import kotlinx.android.synthetic.main.fragment_splash_anim.*

class SplashFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!Application.shouldShowSplash) {
            this.findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
            Application.shouldShowSplash = true
        }
    }

    private lateinit var viewModel: SplashViewModel
    private lateinit var splashViewModelFactory: SplashViewModelFactory
    private lateinit var sharedPreference: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view =  inflater.inflate(R.layout.fragment_splash, container, false)


        splashViewModelFactory = SplashViewModelFactory(activity?.applicationContext!!)
        viewModel = ViewModelProvider(this, splashViewModelFactory).get(SplashViewModel::class.java)

        //Disable action bar
        val activityObj = activity as AppCompatActivity
        activityObj.supportActionBar?.hide()


        viewModel.navigateToMain.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(markSMSPermission() && markLocationPermission() && it) {
                    this.findNavController()
                        .navigate(SplashFragmentDirections.actionSplashFragmentToMainFragment())

                }else {
                    this.findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
                }
                viewModel.doneNavigateToMain()
            }
        })

        // Inflate the layout for this fragment
        return view
    }

    private fun markSMSPermission(): Boolean{
        if (ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun markLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
}