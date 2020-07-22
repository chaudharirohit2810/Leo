package com.rohit2810.leo_kotlin.ui.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rohit2810.leo_kotlin.R
import com.rohit2810.leo_kotlin.databinding.LayourMainBinding
import com.rohit2810.leo_kotlin.services.FallDetectionService
import com.rohit2810.leo_kotlin.services.LocationService
import com.rohit2810.leo_kotlin.services.wifidirect.WiFiP2PServiceLeo
import com.rohit2810.leo_kotlin.ui.BottomSheetDialogLeo
import com.rohit2810.leo_kotlin.utils.connectP2P
import com.rohit2810.leo_kotlin.utils.getFallDetectionPrefs
import com.rohit2810.leo_kotlin.utils.showToast
import timber.log.Timber

class MainFragment : Fragment() {

    private lateinit var viewmodel: MainViewModel
    private lateinit var fadeIn: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService()

        val manager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = LayourMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        fadeIn = AnimationUtils.loadAnimation(activity?.applicationContext, R.anim.fade_in)

        requireActivity().title = "Leo"


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

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
                this.findNavController()
                    .navigate(MainFragmentDirections.actionMainFragmentToMapsFragment())
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

        // To unMark the trouble
        viewmodel.unMarkTrouble.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewmodel.unMarkTrouble()
                viewmodel.doneUnMarkTrouble()
            }
        })

        //Show precautions Dialog
        viewmodel.showPrecautionsDialog.observe(viewLifecycleOwner, Observer {
            it?.let {
                BottomSheetDialogLeo().show(activity?.supportFragmentManager!!, "Precautions Bottom Sheet")
                viewmodel.doneShowPrecautionsDialog()
            }
        })

        viewmodel.navigateToNews.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToNewsFragment())
                viewmodel.doneNavigateToNews()
            }
        })


        viewmodel.toastMsg.observe(viewLifecycleOwner, Observer {
            it?.let {
                activity?.showToast(it)
                viewmodel.doneShowingToastMsg()
            }
        })

        viewmodel.showNotInTrouble.observe(viewLifecycleOwner, Observer { trou->
            viewmodel.isProgressBarVisible.observe(viewLifecycleOwner, Observer { prog->
                if(!(prog || trou)) {
                    binding.btnTrouble.visibility = View.VISIBLE
                }else {
                    binding.btnTrouble.visibility = View.GONE
                }
            })
        })

        viewmodel.connectToP2P.observe(viewLifecycleOwner, Observer {
            it?.let {
                Timber.d("Inside viewmodel p2p")
                requireActivity().connectP2P()
                viewmodel.doneConnectToP2P()
            }
        })


        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_menu_settings -> {
                this.findNavController()
                    .navigate(MainFragmentDirections.actionMainFragmentToSettingsFragment())
            }
        }
        return true
    }

    private fun startService() {
        if (getFallDetectionPrefs(activity?.applicationContext!!)) {
            val intent = Intent(activity?.applicationContext, FallDetectionService::class.java)
            ContextCompat.startForegroundService(activity?.applicationContext!!, intent)
        }
        val intent2 = Intent(activity?.applicationContext, LocationService::class.java)
        ContextCompat.startForegroundService(activity?.applicationContext!!, intent2)
        val intent3 = Intent(activity?.applicationContext, WiFiP2PServiceLeo::class.java)
        ContextCompat.startForegroundService(activity?.applicationContext!!, intent3)
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(requireActivity()!!);
        builder.setTitle("Enable GPS")
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        builder.setNegativeButton("NO") { dialog, which ->
            dialog.cancel()
        }

        val alert: AlertDialog = builder.create();
        alert.show();
    }



}