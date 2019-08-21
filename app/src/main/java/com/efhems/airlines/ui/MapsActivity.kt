package com.efhems.airlines.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.efhems.airlines.databinding.ActivityMapsBinding
import com.efhems.airlines.domain.Airport
import com.efhems.airlines.domain.Schedule
import com.efhems.airlines.util.hideKeyboard
import com.efhems.airlines.viewmodels.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    ScheduleRCAdapter.OnScheculeClickListener {
    
    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private val TAG = MapsActivity::class.java.name
    private lateinit var mMap: GoogleMap

    private var airportOrigin: Airport? = null
    private var airPortDest: Airport? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_maps)
        val binding: ActivityMapsBinding =
            DataBindingUtil.setContentView(this, com.efhems.airlines.R.layout.activity_maps)

        val viewmodel = ViewModelProvider.AndroidViewModelFactory(this.application).create(ViewModel::class.java)

        binding.cont.origin.threshold = 1
        binding.cont.dest.threshold = 1
        viewmodel.airport.observe(this, Observer {
            var adapter = CustomListAdapter(this, com.efhems.airlines.R.layout.spinner_item_layout,
                it as ArrayList<Airport>)
            binding.cont.origin.setAdapter(adapter)
            binding.cont.dest.setAdapter(adapter)
            Log.i(TAG, "size is "+ it.size)
        })

        val adapter = ScheduleRCAdapter(this)
        binding.cont.bottomSheetHeader.rcTagsItems.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.cont.bottomSheetHeader.rcTagsItems.setHasFixedSize(true)
        binding.cont.bottomSheetHeader.rcTagsItems.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )



        binding.cont.origin.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
            airportOrigin = p0.getItemAtPosition(p2) as Airport
            binding.cont.origin.setText(airportOrigin?.name)
        }

        binding.cont.dest.onItemClickListener = AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
            airPortDest = p0.getItemAtPosition(p2) as Airport
            binding.cont.dest.setText(airPortDest?.name)
        }

        viewmodel.schedules.observe(this, Observer {
            it?.let {

                Log.i(TAG, "size of schedule is " + it.size)
                binding.cont.bottomSheetHeader.spinKit.visibility = GONE
                adapter.submitList(it)
                binding.cont.bottomSheetHeader.rcTagsItems.adapter = adapter
            }
        })

        bottomSheetSetUp(binding, viewmodel)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(com.efhems.airlines.R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10F), 2000, null)

        //enable the zoom in/zoom out interface on the map
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
    }


    private fun bottomSheetSetUp(binding: ActivityMapsBinding, viewmodel: ViewModel) {
        val sheetBehavior = BottomSheetBehavior.from(binding.cont.bottomSheetHeader.bottomSheet)

        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.cont.searchBtn.setOnClickListener {

            hideKeyboard(this)

            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

            if (airportOrigin != null && airPortDest != null) {
                viewmodel.schedules(airportOrigin!!.name, airPortDest!!.name, "2019-08-22")
            } else {
                Toast.makeText(this, "Choose a valid Airport", Toast.LENGTH_LONG).show()
            }
        }

        binding.cont.bottomSheetHeader.dismissDialog.setOnClickListener {
            sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        }


        // callback for do something
        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(view: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        //btn_bottom_sheet.setText("Close Sheet")
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        //btn_bottom_sheet.setText("Expand Sheet")
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    }
                }
            }
            override fun onSlide(view: View, v: Float) {

            }
        })
    }

    override fun onClick(schedule: Schedule) {

    }

}
