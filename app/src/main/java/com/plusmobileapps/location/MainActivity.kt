package com.plusmobileapps.location

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.TypedValue
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    companion object {
        const val FINE_LOCATION_REQUEST_CODE = 123
    }

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        fab.setOnClickListener {
            checkLocationPermissions()
        }

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap: GoogleMap? ->
            this.googleMap = googleMap ?: return@getMapAsync
            val paddingBottom = 60.dpToInt(this)
            googleMap.apply {
                //move the google logo and other buttons to render above the fab
                setPadding(0, 0, 0, paddingBottom)
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            FINE_LOCATION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLastKnownLocationAndUpdateMap()
                } else {
                    setShouldShowStatus(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
            else -> {
            }
        }
    }

    private fun checkLocationPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        when {
            isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION,this) -> getLastKnownLocationAndUpdateMap()
            neverAskAgainSelected(Manifest.permission.ACCESS_FINE_LOCATION) -> showLinkToSettingsToEnableLocationDialog()
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> showRationaleDialogForLocationPermission()
            else -> requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            FINE_LOCATION_REQUEST_CODE
        )
    }

    private fun showRationaleDialogForLocationPermission() {
        AlertDialog.Builder(this)
            .setMessage("Access to Location is needed in order to geo locate yourself on the map")
            .setPositiveButton("Retry") { _, _ -> requestLocationPermission() }
            .setNegativeButton("No thanks") { _, _ -> }
            .show()
    }

    private fun showLinkToSettingsToEnableLocationDialog() {
        val builder = AlertDialog.Builder(this)
            .setTitle("You must enable location permissions in order to geo locate yourself on the map")
            .setMessage("Enable through permission and check the switch for location")
            .setPositiveButton("Go to Settings") { dialog, which ->
                //link to settings
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            }
            .setNegativeButton("No thanks") { dialog, which ->
                //close dialog
            }
        builder.show()
    }

    private fun getLastKnownLocationAndUpdateMap() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            location ?: return@addOnSuccessListener
            val latLng = LatLng(location.latitude, location.longitude)
            googleMap.addMarker(MarkerOptions().apply {
                position(latLng)
            })
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    
}
