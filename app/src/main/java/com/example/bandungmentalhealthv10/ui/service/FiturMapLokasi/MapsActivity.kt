package com.example.bandungmentalhealthv10.ui.service.FiturMapLokasi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.bandungmentalhealthv10.R
import com.example.bandungmentalhealthv10.databinding.ActivityMapsBinding
import com.example.bandungmentalhealthv10.utils.FunPindah
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // kembali
        binding.kembali.setOnClickListener {
            FunPindah.onBackPressed(this)
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnDrawState.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.activity_maps_confirm, null)

            builder.setView(dialogView)
            val dialog = builder.create()

            dialogView.findViewById<Button>(R.id.btnReset).setOnClickListener {
                getLastKnownLocation { location ->
                    if (location != null) {
                        val coordinates = "${location.latitude},${location.longitude}"
                        performMapSearch(coordinates, "Layanan Kesehatan Mental Terdekat")
                    }
                }
                dialog.dismiss()
            }
            dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }
            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Enable zoom controls
        mMap.uiSettings.isZoomControlsEnabled = true

        // Set up map settings
        val uiSettings: UiSettings = mMap.uiSettings
        uiSettings.isZoomGesturesEnabled = true
        uiSettings.isScrollGesturesEnabled = true
        uiSettings.isRotateGesturesEnabled = true
        uiSettings.isTiltGesturesEnabled = true

        // Get current location and move camera
        getLastKnownLocation { location ->
            if (location != null) {
                val currentLocation = LatLng(location.latitude, location.longitude)
                mMap.addMarker(MarkerOptions().position(currentLocation).title("Lokasi Anda"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12.0f))
            }
        }
    }

    private fun getLastKnownLocation(callback: (android.location.Location?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: android.location.Location? ->
            callback(location)
        }
    }

    private fun performMapSearch(coordinates: String, query: String) {
        try {
            // Create the Google Maps URL with a search query for the nearest hospital
            val mapsUrl = "https://www.google.com/maps/search/$query/@$coordinates,14.94z"

            // Open the URL in a browser
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl))
            startActivity(intent)
        } catch (e: Exception) {
            // Handle exception
        }
    }
}