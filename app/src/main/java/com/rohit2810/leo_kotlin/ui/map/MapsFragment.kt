package com.rohit2810.leo_kotlin.ui.map

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rohit2810.leo_kotlin.R
import com.rohit2810.leo_kotlin.database.getDatabase
import com.rohit2810.leo_kotlin.utils.getLatitudeFromCache
import com.rohit2810.leo_kotlin.utils.getLongitudeFromCache
import timber.log.Timber

class MapsFragment : Fragment() {

    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var mapsViewModelFactory: MapsViewModelFactory
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val manager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var acti = requireActivity() as AppCompatActivity
        acti.supportActionBar?.hide()

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

    }

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        val latitude = getLatitudeFromCache(activity?.applicationContext!!)
        val longitude = getLongitudeFromCache(activity?.applicationContext!!)
        val current = LatLng(latitude.toDouble(), longitude.toDouble())
        val current2 = LatLng(latitude.toDouble(), longitude + 0.008)
        val current3 = LatLng(latitude + 0.01, longitude.toDouble())
        googleMap.addMarker(MarkerOptions().position(current).title("Current Location"))
//        markHeatmap(googleMap, current2)
//        markHeatmap(googleMap, current3)
        getDatabase(requireContext()).heatmapDao.getAllHeatmaps()
            .observe(viewLifecycleOwner, Observer {
                it?.let {
                    it.map { it2 ->
                        Timber.d(it2.toString())
                        val loc = LatLng(it2.latitude, it2.longitude)
                        markHeatmap(mMap, loc, count = it2.count)
                    }
                }
            })
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 14.5f))
    }

    private fun markHeatmap(
        googleMap: GoogleMap,
        current: LatLng,
        radius: Double = 500.0,
        count: Int = 25
    ) {
        var color : Int = colorUtil(count)

        googleMap.addCircle(
            CircleOptions().center(current).radius(radius).fillColor(ContextCompat.getColor(requireContext(), color))
                .strokeWidth(0f)
                .clickable(true)
        )
    }

    fun colorUtil( count: Int): Int {
        if(count < 10) {
            return R.color.leo_maps_red_color1
        }else if (count < 20) {
            return R.color.leo_maps_red_color2
        }else if(count < 30) {
            return R.color.leo_maps_red_color3
        }else if (count < 40) {
            return R.color.leo_maps_red_color4
        }
        return R.color.leo_maps_red_color5
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapsViewModelFactory = MapsViewModelFactory(requireContext())
        mapsViewModel = ViewModelProvider(this, mapsViewModelFactory).get(MapsViewModel::class.java)

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