package com.example.leo_kotlin.ui.splash

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.leo_kotlin.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SplashFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SplashFragment : Fragment() {

    private lateinit var viewModel: SplashViewModel
    private lateinit var splashViewModelFactory: SplashViewModelFactory
    private lateinit var sharedPreference: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        splashViewModelFactory = SplashViewModelFactory(activity?.applicationContext!!)
        viewModel = ViewModelProvider(this, splashViewModelFactory).get(SplashViewModel::class.java)

        //Disable action bar
        val activityObj = activity as AppCompatActivity
        activityObj.supportActionBar?.hide()





        viewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
                viewModel.doneNavigateToLogin()
            }
        })

        viewModel.navigateToMain.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToMainFragment())
                viewModel.doneNavigateToMain()
            }
        })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}