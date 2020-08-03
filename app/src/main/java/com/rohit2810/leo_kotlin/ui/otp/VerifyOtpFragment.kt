package com.rohit2810.leo_kotlin.ui.otp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rohit2810.leo_kotlin.databinding.FragmentVerifyOtpBinding
import com.rohit2810.leo_kotlin.models.user.User
import com.rohit2810.leo_kotlin.utils.showToast
import timber.log.Timber

class VerifyOtpFragment : Fragment() {

    private lateinit var user: User
    private var isFromForgot: Boolean = false
    private lateinit var viewModel: VerifyOtpViewModel
    private lateinit var viewModelFactory: VerifyOtpViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = VerifyOtpFragmentArgs.fromBundle(requireArguments()).registeredUser
        isFromForgot = VerifyOtpFragmentArgs.fromBundle(requireArguments()).isFromForgot
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentVerifyOtpBinding.inflate(inflater, container, false)

        viewModelFactory = VerifyOtpViewModelFactory(activity?.applicationContext!!, user)
        viewModel = ViewModelProvider(this, viewModelFactory).get(VerifyOtpViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.registeredUser.observe(viewLifecycleOwner, Observer {
            it?.let {
                Timber.d(it.toString())
                if (isFromForgot) {
                    this.findNavController().navigate(
                        VerifyOtpFragmentDirections.actionVerifyOtpFragmentToChangePasswordFragment(it)
                    )
                }else {
                    this.findNavController().navigate(
                        VerifyOtpFragmentDirections.actionVerifyOtpFragmentToEmergencyContactsFragment(
                            it,
                            false
                        )
                    )
                }
            }
        })

        viewModel.toastMsg.observe(viewLifecycleOwner, Observer {
            it?.let {
                activity?.showToast(it)
                viewModel.doneShowingToastMsg()
            }
        })

        return binding.root
    }

}