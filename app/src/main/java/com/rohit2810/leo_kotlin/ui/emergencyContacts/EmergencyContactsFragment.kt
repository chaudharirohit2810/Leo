package com.rohit2810.leo_kotlin.ui.emergencyContacts

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rohit2810.leo_kotlin.databinding.FragmentEmergencyContactsBinding
import com.rohit2810.leo_kotlin.models.user.User
import com.rohit2810.leo_kotlin.repository.TroubleRepository
import com.rohit2810.leo_kotlin.utils.showToast

class EmergencyContactsFragment : Fragment() {

    private lateinit var viewModel: EmergencyContactsViewModel

    private lateinit var user: User

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = EmergencyContactsFragmentArgs.fromBundle(arguments!!).registeredUser
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEmergencyContactsBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this

        val toUpdate = EmergencyContactsFragmentArgs.fromBundle(requireArguments()).isFromMain

        requireActivity().title = "Leo"

//        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        val repository = TroubleRepository.getInstance(activity?.applicationContext!!)
        val viewModelFactory = EmergencyContactsViewModelFactory(
            user,
            repository,
            activity?.applicationContext!!,
            toUpdate
        )
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(EmergencyContactsViewModel::class.java)

        binding.viewmodel = viewModel

        requireActivity().title = "Emergency Contacts"

        viewModel.toastMsg.observe(viewLifecycleOwner, Observer {
            it?.let {
//                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                activity?.showToast(it)
                viewModel.doneShowingToastMsg()
            }
        })

        viewModel.navigateToMain.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (!markSMSPermission() || !markLocationPermission()) {
                    this.findNavController()
                        .navigate(EmergencyContactsFragmentDirections.actionEmergencyContactsFragmentToPermissionRequestFragment2())
                } else if (!toUpdate) {
                    this.findNavController()
                        .navigate(EmergencyContactsFragmentDirections.actionEmergencyContactsFragmentToMainFragment())
                } else {
                    this.findNavController().navigateUp()
                }
                viewModel.doneNavigateToMain()
            }
        })

        viewModel.snackbarMsg.observe(viewLifecycleOwner, Observer {
            it?.let {
                activity?.showToast(it)
                viewModel.doneShowingSnackbar()
            }
        })




        return binding.root
    }

    private fun markSMSPermission(): Boolean {
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