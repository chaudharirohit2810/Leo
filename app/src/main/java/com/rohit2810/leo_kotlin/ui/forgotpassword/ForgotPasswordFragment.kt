package com.rohit2810.leo_kotlin.ui.forgotpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rohit2810.leo_kotlin.R
import com.rohit2810.leo_kotlin.databinding.FragmentForgotPasswordBinding
import com.rohit2810.leo_kotlin.ui.feedback.FeedbackViewModel
import com.rohit2810.leo_kotlin.ui.feedback.FeedbackViewModelFactory
import com.rohit2810.leo_kotlin.utils.showToast

class ForgotPasswordFragment : Fragment() {

    private lateinit var viewModel: ForgotPasswordViewModel
    private lateinit var feedbackViewModelFactory: ForgotPasswordViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        feedbackViewModelFactory = ForgotPasswordViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, feedbackViewModelFactory).get(ForgotPasswordViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        viewModel.registeredUser.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToVerifyOtpFragment(it, true))
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