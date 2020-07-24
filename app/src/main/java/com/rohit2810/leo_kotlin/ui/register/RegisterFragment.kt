package com.rohit2810.leo_kotlin.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rohit2810.leo_kotlin.databinding.FragmentRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.rohit2810.leo_kotlin.utils.showToast

class RegisterFragment : Fragment() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var viewModelFactory: RegisterViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentRegisterBinding.inflate(inflater, container, false)


        requireActivity().title = "Register"

        viewModelFactory = RegisterViewModelFactory(activity?.applicationContext!!)

        viewModel = ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel


        viewModel.registeredUser.observe(viewLifecycleOwner, Observer {
            it?.let {
//                this.findNavController().navigate()
                this.findNavController().navigate(
                    RegisterFragmentDirections.actionRegisterFragmentToVerifyOtpFragment2(it)
                )
                viewModel.doneNavigateToEmergencyNumbers()
            }
        })

        viewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigateUp()
                viewModel.doneNavigateToLogin()
            }
        })

        viewModel.toastMsg.observe(viewLifecycleOwner, Observer {
            it?.let {
                activity?.showToast(it)
                viewModel.doneShowingToastMsg()
            }
        })
        // Inflate the layout for this fragment
        return binding.root
    }

}