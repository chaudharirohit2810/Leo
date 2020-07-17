package com.rohit2810.leo_kotlin.ui.login

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rohit2810.leo_kotlin.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.rohit2810.leo_kotlin.utils.showToast

class LoginFragment : Fragment() {

    private lateinit var viewmodel: LoginViewModel
    private lateinit var loginViewModelFactory: LoginViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        loginViewModelFactory = LoginViewModelFactory(activity?.applicationContext!!)
        viewmodel = ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java)
        binding.viewmodel = viewmodel

        requireActivity().title = "Login"

        //Disable action bar
        val activityObj = activity as AppCompatActivity
        activityObj.supportActionBar?.show()

        viewmodel.navigateToRegister.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
                viewmodel.doneNavigateToRegister()
            }
        })

        viewmodel.toastMsg.observe(viewLifecycleOwner, Observer {
            it?.let {
//                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                activity?.showToast(it)
                viewmodel.doneToastMsg()
            }
        })

        viewmodel.navigateToMain.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(!markSMSPermission() || !markLocationPermission()) {
                    this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToPermissionRequestFragment2())
                }else {
                    this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
                }
                viewmodel.doneNavigateToMain()
            }
        })

        viewmodel.snackbarMsg.observe(viewLifecycleOwner, Observer {
            it?.let {
                activity?.showToast(it)
                viewmodel.doneShowingSnackBar()
            }
        })


        return binding.root
    }

    private fun markSMSPermission(): Boolean{
        if (ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                android.Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun markLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }


}