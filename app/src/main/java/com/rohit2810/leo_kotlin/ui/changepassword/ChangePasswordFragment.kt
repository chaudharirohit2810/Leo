package com.rohit2810.leo_kotlin.ui.changepassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rohit2810.leo_kotlin.R
import com.rohit2810.leo_kotlin.databinding.FragmentChangePasswordBinding
import com.rohit2810.leo_kotlin.models.user.User
import com.rohit2810.leo_kotlin.ui.otp.VerifyOtpFragmentArgs
import com.rohit2810.leo_kotlin.utils.showToast

class ChangePasswordFragment : Fragment() {

    private lateinit var user: User
    private lateinit var viewModel: ChangePasswordViewModel
    private lateinit var viewModelFactory: ChangePasswordViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user =  ChangePasswordFragmentArgs.fromBundle(requireArguments()).registeredUser
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        viewModelFactory = ChangePasswordViewModelFactory(requireContext(), user)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ChangePasswordViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        viewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(ChangePasswordFragmentDirections.actionChangePasswordFragmentToLoginFragment())
                viewModel.navigateToLogin.value = null
            }
        })

        viewModel.toastMsg.observe(viewLifecycleOwner, Observer {
            it?.let {
                activity?.showToast(it)
                viewModel.doneToastMsg()
            }
        })
        // Inflate the layout for this fragment
        return binding.root
    }

}