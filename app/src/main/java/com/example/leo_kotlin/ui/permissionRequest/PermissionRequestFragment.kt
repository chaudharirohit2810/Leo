package com.example.leo_kotlin.ui.permissionRequest

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.leo_kotlin.databinding.FragmentPermissionRequestBinding
import com.example.leo_kotlin.utils.addPermissionPreference
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class PermissionRequestFragment : Fragment() {

    private lateinit var viewModel: PermissionRequestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val binding = Frag
        val binding = FragmentPermissionRequestBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(PermissionRequestViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        binding.switchLocation.setOnClickListener {
            requestLocationPermission()
        }

        binding.switchSms.setOnClickListener {
            requestSMSPermission()
        }

        viewModel.navigateToMain.observe(viewLifecycleOwner, Observer {
            it?.let {
                addPermissionPreference(activity?.applicationContext!!)
                this.findNavController()
                    .navigate(PermissionRequestFragmentDirections.actionPermissionRequestFragmentToMainFragment2())
                viewModel.doneNavigateToMain()
            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!, Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            }
        } else {
            viewModel.locationPermissionGranted()
        }
    }


    private fun requestSMSPermission() {
        if (ContextCompat.checkSelfPermission(
                activity?.applicationContext!!,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!, Manifest.permission.SEND_SMS
                )
            ) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.SEND_SMS), 2
                )
            } else {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.SEND_SMS), 2
                )
            }
        } else {
            viewModel.smsPermissionGranted()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Timber.d(grantResults.toString())
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                            activity!!,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                                == PackageManager.PERMISSION_GRANTED)
                    ) {
                        viewModel.locationPermissionGranted()
                    } else {
                        viewModel.locationPermissionNotGranted()
                        Snackbar.make(
                            view!!,
                            "Location permission is mandatory",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    return
                }
            }
            2 -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(activity!!, Manifest.permission.SEND_SMS)
                                == PackageManager.PERMISSION_GRANTED)
                    ) {
                        viewModel.smsPermissionGranted()
                    } else {
                        viewModel.smsPermissionNotGranted()
                        Snackbar.make(
                            view!!,
                            "Sms permission is required to notify your emergency contacts",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    return
                }
            }
        }
    }

}