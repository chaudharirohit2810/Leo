package com.example.leo_kotlin.ui.emergencyContacts

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.leo_kotlin.R
import com.example.leo_kotlin.databinding.FragmentEmergencyContactsBinding
import com.example.leo_kotlin.models.User
import com.example.leo_kotlin.repository.TroubleRepository
import com.example.leo_kotlin.ui.login.LoginFragmentDirections
import com.example.leo_kotlin.utils.getPermissionPreference
import com.google.android.material.snackbar.Snackbar

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

        val toUpdate = EmergencyContactsFragmentArgs.fromBundle(arguments!!).isFromMain

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(toUpdate) {
                    var options = NavOptions.Builder().setEnterAnim(R.anim.slide_in_left)
                        .setExitAnim(R.anim.slide_out_right).build()
                    this@EmergencyContactsFragment.findNavController().navigate(
                        R.id.action_emergencyContactsFragment_to_mainFragment, null, options
                    )
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

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

        viewModel.toastMsg.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                viewModel.doneShowingToastMsg()
            }
        })

        viewModel.navigateToMain.observe(viewLifecycleOwner, Observer {
            it?.let {
                var options: NavOptions? = null
                if (toUpdate) {
                    options = NavOptions.Builder().setEnterAnim(R.anim.slide_in_left)
                        .setExitAnim(R.anim.slide_out_right).build()
                } else {
                    options = NavOptions.Builder().setEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_left).build()
                }
                if (!getPermissionPreference(activity?.applicationContext!!)) {
                    this.findNavController().navigate(
                        R.id.action_emergencyContactsFragment_to_permissionRequestFragment2,
                        null,
                        options
                    )
                } else {
                    this.findNavController().navigate(
                        R.id.action_emergencyContactsFragment_to_mainFragment,
                        null,
                        options
                    )
                }
                viewModel.doneNavigateToMain()
            }
        })

        viewModel.snackbarMsg.observe(viewLifecycleOwner, Observer {
            it?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
                viewModel.doneShowingSnackbar()
            }
        })




        return binding.root
    }


}