package com.rohit2810.leo_kotlin.ui.routes

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rohit2810.leo_kotlin.R
import com.rohit2810.leo_kotlin.database.getDatabase
import com.rohit2810.leo_kotlin.ui.BottomSheetDialogLeo
import com.rohit2810.leo_kotlin.utils.showToast
import kotlinx.android.synthetic.main.fragment_routes.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class RoutesFragment : Fragment(), GoogleMap.OnCircleClickListener {

    private lateinit var routeViewModel: RouteViewModel
    private lateinit var routesViewModelFactory: RoutesViewModelFactory

    private val callback = OnMapReadyCallback { googleMap ->
        var lat = 18.5314
        var long = 73.8446
        val sydney = LatLng(lat, long)

        googleMap.addMarker(MarkerOptions().position(sydney).title("Your Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14.5f))

        getDatabase(requireContext()).heatmapDao.getDirections()
            .observe(viewLifecycleOwner, Observer {
                it?.let {
                    if (it.isEmpty()) {
                        return@Observer;
                    }
                    var polyline = PolylineOptions().width(10f).color(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                    var i = 0
                    var set = mutableSetOf<LatLng>()
                    for (route in it) {
                        var loc = LatLng(route.latitude, route.longitude)
                        if (!set.contains(loc)) {
                            polyline.add(loc)
                            set.add(loc)
                        }
                    }
                    googleMap.addPolyline(polyline)
                    set.clear()
                }
            })
        getDatabase(requireContext()).heatmapDao.getUnsafe().observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isEmpty()) {
                    return@Observer
                }
                for (ele in it) {
                    markHeatmap(googleMap, LatLng(ele.Latitude, ele.Longitude), ele.id)
                }
            }
        })
        googleMap.setOnCircleClickListener{
            Timber.d(it.tag.toString())
            var ele = getDatabase(requireContext()).heatmapDao.getSingleUnsafe(it.tag as Int)
            ele.observe(viewLifecycleOwner, Observer {
                it?.let {
                    BottomSheetDialogNews(it).show(
                        activity?.supportFragmentManager!!,
                        "News"
                    )
                }
            })
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_routes, container, false)
        routesViewModelFactory = RoutesViewModelFactory(requireContext())
        routeViewModel = ViewModelProvider(this, routesViewModelFactory).get(RouteViewModel::class.java)
        var dialog = view.findViewById<LinearLayout>(R.id.dialog)
        var map = view.findViewById<View>(R.id.map)
        var navigateButton = view.findViewById<Button>(R.id.btn_navigate)
        var start_point = view.findViewById<EditText>(R.id.edit_text_start_point)
        var end_point = view.findViewById<EditText>(R.id.edit_text_end_point)
        var progressBar = view.findViewById<ProgressBar>(R.id.map_progress_bar)



        routeViewModel.isDialogVisible.observe(viewLifecycleOwner, Observer {it1 ->
                it1.let {
                        if (it1) {
                            map.visibility = View.INVISIBLE
                            dialog.visibility = View.VISIBLE
                        } else {
                            map.visibility = View.VISIBLE
                            dialog.visibility = View.INVISIBLE
                        }
                }
        })

        routeViewModel.isProgressBarVisible.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                }else {
                    progressBar.visibility = View.INVISIBLE
                }
            }
        })

        navigateButton.setOnClickListener {
            if (start_point.text.isNullOrEmpty() || end_point.text.isNullOrEmpty()) {
                activity?.showToast("Please enter end point and start point")
                return@setOnClickListener
            }
            routeViewModel.disableDialog()
            var lat1 = 18.5314
            var long1 = 73.8446
            var lat2 = 18.5808
            var long2 = 73.9787
            routeViewModel.getDirections(lat1, long1, lat2, long2)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }

    private fun markHeatmap(
        googleMap: GoogleMap,
        current: LatLng,
        id: Int,
        radius: Double = 500.0,
        count: Int = 25
    ) {
        var circle = googleMap.addCircle(
            CircleOptions().center(current).radius(radius).fillColor(ContextCompat.getColor(requireContext(), R.color.leo_maps_red_color2))
                .strokeWidth(1f)
                .clickable(true)
        )
        circle.tag = id
        circle.isClickable = true

    }

    override fun onCircleClick(p0: Circle?) {
        Timber.d(p0.toString())
    }
}