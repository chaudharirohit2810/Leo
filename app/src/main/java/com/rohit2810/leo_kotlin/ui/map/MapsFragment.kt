package com.rohit2810.leo_kotlin.ui.map

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.rohit2810.leo_kotlin.R
import com.rohit2810.leo_kotlin.utils.getLatitudeFromCache
import com.rohit2810.leo_kotlin.utils.getLongitudeFromCache

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val manager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

    }

    private val callback = OnMapReadyCallback { googleMap ->
        val latitude = getLatitudeFromCache(activity?.applicationContext!!)
        val longitude = getLongitudeFromCache(activity?.applicationContext!!)
        val sydney = LatLng(latitude.toDouble(), longitude.toDouble())
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16f))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(requireActivity()!!);
        builder.setTitle("Enable GPS")
        builder.setMessage("GPS is required to see crime hotspots near your area")
            .setCancelable(false)
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        builder.setNegativeButton("NO") { dialog, which ->
            dialog.cancel()
        }

        val alert: AlertDialog = builder.create();
        alert.show();
    }
}