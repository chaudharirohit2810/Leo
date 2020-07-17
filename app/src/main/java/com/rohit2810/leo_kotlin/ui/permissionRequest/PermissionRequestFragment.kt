package com.rohit2810.leo_kotlin.ui.permissionRequest

import android.Manifest
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
import com.rohit2810.leo_kotlin.databinding.FragmentPermissionRequestBinding
import com.google.android.material.snackbar.Snackbar
import com.rohit2810.leo_kotlin.utils.showToast
import timber.log.Timber

class PermissionRequestFragment : Fragment() {

    private lateinit var viewModel: PermissionRequestViewModel
    private lateinit var binding: FragmentPermissionRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val binding = Frag
         binding = FragmentPermissionRequestBinding.inflate(inflater, container, false)
        activity?.title = "Permissions"
        viewModel = ViewModelProvider(this).get(PermissionRequestViewModel::class.java)

//        markLocationPermission()
        if(markLocationPermission()) {
            viewModel.locationPermissionGranted()
            binding.switchLocation.isEnabled = false
        }
        if(markSMSPermission()) {
            viewModel.smsPermissionGranted()
            binding.switchSms.isEnabled = false
        }
        markSMSPermission()


        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        viewModel.isLocationPermissionGranted.observe(viewLifecycleOwner, Observer { it1 ->
            it1?.let { loc ->
                viewModel.isSMSPermissionGranted.observe(viewLifecycleOwner, Observer {it2->
                    it2?.let { sms ->
                        if(loc && sms) {
                            viewModel.allPermissionsGranted()
                        }
                    }

                })
            }
        })

        binding.switchLocation.setOnClickListener {
            requestLocationPermission()
        }

        binding.switchSms.setOnClickListener {
//            if ()
            requestSMSPermission()
        }

        viewModel.navigateToMain.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController()
                    .navigate(PermissionRequestFragmentDirections.actionPermissionRequestFragmentToMainFragment2())
                viewModel.doneNavigateToMain()
            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun markLocationPermission() : Boolean {
        if (ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
//            Timber.d("Permission already granted")
//            viewModel.locationPermissionGranted()
            return true
        }
        return false
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), 1
                )
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1
                )
            }
        }
    }

    private fun markSMSPermission() : Boolean {
        if (ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
//            viewModel.smsPermissionGranted()
            return true
        }
        return false
    }


    private fun requestSMSPermission() {
        if (ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.SEND_SMS
                )
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.SEND_SMS), 2
                )
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.SEND_SMS), 2
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Timber.d(grantResults.toString())
        when (requestCode) {
            1 -> {
                if ((ContextCompat.checkSelfPermission(
                        activity!!,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                            == PackageManager.PERMISSION_GRANTED)
                ) {
                    viewModel.locationPermissionGranted()
                    binding.switchLocation.isEnabled = false
                } else {
                    viewModel.locationPermissionNotGranted()
                    activity?.showToast("Location permission is mandatory")
                }
                return
            }
            2 -> {
                if ((ContextCompat.checkSelfPermission(activity!!, Manifest.permission.SEND_SMS)
                            == PackageManager.PERMISSION_GRANTED)
                ) {
                    viewModel.smsPermissionGranted()
                    binding.switchSms.isEnabled = false
                } else {
                    viewModel.smsPermissionNotGranted()
                    activity?.showToast("Sms permission is required to notify your emergency contacts")
                }
                return
            }
        }
    }

}