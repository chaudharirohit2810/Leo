package com.example.leo_kotlin.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.leo_kotlin.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by lazy {
        ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        viewModel.registeredUser.observe(viewLifecycleOwner, Observer {
            it?.let {
//                this.findNavController().navigate()
                this.findNavController().navigate(
                    RegisterFragmentDirections.actionRegisterFragmentToEmergencyContactsFragment(it, false)
                )
                viewModel.doneNavigateToEmergencyNumbers()
            }
        })

        viewModel.toastMsg.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                viewModel.doneShowingToastMsg()
            }
        })
        // Inflate the layout for this fragment
        return binding.root
    }

}