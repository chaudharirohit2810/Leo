package com.example.leo_kotlin.ui.login

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.leo_kotlin.databinding.FragmentLoginBinding
import com.example.leo_kotlin.utils.getPermissionPreference
import com.google.android.material.snackbar.Snackbar
import java.util.jar.Manifest

class LoginFragment : Fragment() {

    private lateinit var viewmodel: LoginViewModel
    private lateinit var loginViewModelFactory: LoginViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        loginViewModelFactory = LoginViewModelFactory(activity?.applicationContext!!)
        viewmodel = ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java)
        binding.viewmodel = viewmodel


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
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                viewmodel.doneToastMsg()
            }
        })

        viewmodel.navigateToMain.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(!getPermissionPreference(activity?.applicationContext!!)) {
                    this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToPermissionRequestFragment2())
                }else {
                    this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
                }

                viewmodel.doneNavigateToMain()
            }
        })

        viewmodel.snackbarMsg.observe(viewLifecycleOwner, Observer {
            it?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                    .show()

                viewmodel.doneShowingSnackBar()
            }
        })



        return binding.root
    }


}