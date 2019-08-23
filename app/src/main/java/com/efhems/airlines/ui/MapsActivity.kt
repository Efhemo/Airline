package com.efhems.airlines.ui

import android.app.DatePickerDialog
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.efhems.airlines.R
import com.efhems.airlines.databinding.ActivityMapsBinding
import com.efhems.airlines.domain.Airport
import com.efhems.airlines.domain.Schedule
import com.efhems.airlines.util.hideKeyboard
import com.efhems.airlines.viewmodels.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

/**
 * One screen Activity housing the autocompleteTextView and bottomSheet
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    ScheduleRCAdapter.OnScheculeClickListener, DatePickerDialog.OnDateSetListener{

    private lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>


    override fun onMarkerClick(p0: Marker?): Boolean = false
    private lateinit var binding: ActivityMapsBinding

    //Google map instance
    private lateinit var mMap: GoogleMap

    /**
     * airportOrigin and airPortDest can be null
     * */
    private var airportOrigin: Airport? = null
    private var airPortDest: Airport? = null

    private lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_maps)

        /**
         *Initialize map activity data binging
         * */
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_maps)

        val viewmodel = ViewModelProvider.AndroidViewModelFactory(this.application).create(ViewModel::class.java)

        /**
         * Set the two autocompleetetextview threshold to 1
         * */
        binding.cont.origin.threshold = 1
        binding.cont.dest.threshold = 1

        //Viemodel observe data from the databse
        viewmodel.airport.observe(this, Observer {
            val adapter = CustomListAdapter(
                this, R.layout.spinner_item_layout,
                it as ArrayList<Airport>
            )

            /**
             * set the autocompleetetextview adapter respectively
             * * */
            binding.cont.origin.setAdapter(adapter)
            binding.cont.dest.setAdapter(adapter)
        })

        //SetUp Recyclerview
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

        //Handle onItemClickListener of the origin autocompleetetextview
        binding.cont.origin.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                airportOrigin = p0.getItemAtPosition(p2) as Airport
                binding.cont.origin.setText(airportOrigin?.name)
            }

        //Handle onItemClickListener of the destination autocompleetetextview
        binding.cont.dest.onItemClickListener = AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
            airPortDest = p0.getItemAtPosition(p2) as Airport
            binding.cont.dest.setText(airPortDest?.name)
        }

        //viewmodel observe if schedule is available
        viewmodel.schedules.observe(this, Observer {

            //If available, display in bottomshet
            if(it != null && it.isNotEmpty()){
                binding.cont.bottomSheetHeader.spinKit.visibility = GONE
                adapter.submitList(it)
                binding.cont.bottomSheetHeader.rcTagsItems.adapter = adapter

            }
            //If not available, display a toast
            else if(it == null ){
                Toast.makeText(this, "No Schedule", Toast.LENGTH_LONG).show()
                binding.cont.bottomSheetHeader.spinKit.visibility = GONE
            }
        })

        //use the current date as the default value for the picker
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        //Get today instance and display as appropraite on the textview
        date = "$year-0$month-$day"
        binding.cont.date.text = date

        //bottomSheetSetUp
        bottomSheetSetUp(binding, viewmodel, adapter)

        //User can set up and select airport schedule date
        binding.cont.pickDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this,
                this, year, month, day)
            datePickerDialog.show()
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10F), 2000, null)

        //enable the zoom in/zoom out interface on the map
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)

    }

    /**
     * let Geocoder get Address using LatLng
     */
    private fun getAddress(latLng: LatLng): String {
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (null != addresses && addresses.isNotEmpty()) {
                address = addresses[0]
                for (i in 0 until address.maxAddressLineIndex) {
                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
                }
            }
        } catch (e: IOException) {
        }

        return addressText
    }

    private fun bottomSheetSetUp(binding: ActivityMapsBinding, viewmodel: ViewModel, adapter: ScheduleRCAdapter) {

        /**
         * initialize BottomSheetBehavior
         * */
        sheetBehavior = BottomSheetBehavior.from(binding.cont.bottomSheetHeader.bottomSheet)

        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        //Handling on clicking search bUtton
        binding.cont.searchBtn.setOnClickListener {

            //hidekeyboard as soon as user start to search
            hideKeyboard(this)

            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

            if (airportOrigin != null && airPortDest != null) {
                viewmodel.schedules(airportOrigin!!.name, airPortDest!!.name, date)
            } else {
                Toast.makeText(this, "Choose a valid Airport", Toast.LENGTH_LONG).show()
            }
        }

        binding.cont.bottomSheetHeader.dismissDialog.setOnClickListener {
            sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> adapter.submitList(null)
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    }
                }
            }

            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    //Update user as user change date
    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        date = "$p1-0$p2-$p3"
        binding.cont.date.text = date
    }


    /**
     * Handling onClick Schedules item
     * */
    override fun onClick(schedule: Schedule) {

        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val list = ArrayList<LatLng>()

        airportOrigin?.let {
            airportOrigin?.let {
                val latLng = LatLng(it.latitude.toDouble(), it.longitude.toDouble())
                list.add(latLng)
            }
        }

        airPortDest?.let {
            airPortDest?.let {
                val latLng = LatLng(it.latitude.toDouble(), it.longitude.toDouble())
                list.add(latLng)
            }
        }

        createPolyLine(list)
    }

    /**
     * Help the user with the polyline
     * */
    private fun createPolyLine(latLnglist: List<LatLng>) {

        val options = PolylineOptions().width(10.toFloat()).color(Color.GREEN).geodesic(true)
        options.addAll(latLnglist)
        mMap.addPolyline(options)

        val builder = LatLngBounds.Builder()

        for (i in latLnglist){
            builder.include(i)
            mMap.addMarker(MarkerOptions().position(i).title(getAddress(i)))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(i))
        }
        val bounds = builder.build()


        val cu = CameraUpdateFactory.newLatLngBounds(bounds, 200)
        mMap.animateCamera(cu)

    }
}
