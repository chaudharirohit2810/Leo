package com.example.leo_kotlin.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.leo_kotlin.R
import com.example.leo_kotlin.databinding.FragmentMainBinding
import com.example.leo_kotlin.services.LocationService
import com.example.leo_kotlin.services.MainService
import com.example.leo_kotlin.utils.getUserFromCache
import com.example.leo_kotlin.utils.removePermissionPreference
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

    private lateinit var viewmodel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        //Disable action bar
        val activityObj = activity as AppCompatActivity
        activityObj.supportActionBar?.show()

        setHasOptionsMenu(true)



        val mainViewModelFactory = MainViewModelFactory(activity?.applicationContext!!)


        viewmodel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)
        binding.viewmodel = viewmodel

        // To navigate to maps
        viewmodel.navigateToMaps.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToMapsFragment())
                viewmodel.doneNavigateToMaps()
            }
        })

        // To mark trouble
        viewmodel.sendTrouble.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewmodel.markTrouble()
                viewmodel.doneSendTrouble()
            }
        })

        // When user logouts
        viewmodel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToLoginFragment2())
                viewmodel.doneNavigateToLogin()
            }
        })

        viewmodel.navigateToEmergencyContacts.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToEmergencyContactsFragment(
                    getUserFromCache(activity?.applicationContext!!)!!, true
                ))
            }
        })

        viewmodel.toastMsg.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                viewmodel.doneShowingToastMsg()
            }
        })

        viewmodel.snackbarMsg.observe(viewLifecycleOwner, Observer {
            it?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                    .show()

                viewmodel.doneShowingSnackbarMsg()
            }
        })


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.main_menu_settings -> {
                viewmodel.logoutUser()
                removePermissionPreference(activity?.applicationContext!!)
                stopService()
            }
            R.id.main_menu_contacts -> viewmodel.goToEmergencyContacts()
        }
        return true
    }

    private fun startService() {
        val intent = Intent(activity?.applicationContext, MainService::class.java)
        ContextCompat.startForegroundService(activity?.applicationContext!!, intent)
        val intent2 = Intent(activity?.applicationContext, LocationService::class.java)
        ContextCompat.startForegroundService(activity?.applicationContext!!, intent2)
    }

    fun stopService() {
        val intent = Intent(activity?.applicationContext!!, MainService::class.java)
        activity?.stopService(intent)
        val intent2 = Intent(activity?.applicationContext!!, LocationService::class.java)
        activity?.stopService(intent2)
    }

}