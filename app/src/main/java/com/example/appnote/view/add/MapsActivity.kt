package com.example.appnote.view.add

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appnote.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var strAddress = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)


        val intent = intent
        val bundle: Bundle? = intent.extras
        strAddress = bundle?.getString("address") ?: "João de Melo 46, Belo Horizonte, Minas Gerais"
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val address = getLocationFromAddress(this, strAddress)
        mMap.addMarker(address?.let { MarkerOptions().position(it).title(strAddress) })
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(address, 15f))
    }

    private fun getLocationFromAddress(context: Context, strAddress: String): LatLng?{
        val coder = Geocoder(context)
        val address: List<Address>
        var p1: LatLng? = null

        try {
            address = coder.getFromLocationName(strAddress, 5)
            if(address != null){
                val location : Address = address.get(0)
                location.latitude
                location.longitude

                p1 = LatLng(location.latitude, location.longitude)
            }
        }catch (e: Exception){
            p1 = LatLng(-34.0, 151.0 )
            mMap.addMarker(MarkerOptions().position(p1).title("O endereço é inválido"))
        }
        return p1
    }
}
