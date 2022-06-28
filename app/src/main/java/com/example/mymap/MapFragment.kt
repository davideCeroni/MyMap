package com.example.mymap

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_map, container, false)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment!!.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            mMap.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(activity!!))
            placeMarkers(mMap)

            mMap.setOnInfoWindowClickListener {
                var currentFightPoint: FightPoint? = null
                for (fightPoint in fightPoints!!) {
                    if(it.position == fightPoint.posizione.toLatLng()) {
                        currentFightPoint = fightPoint
                        break
                    }
                }

                val intent = Intent(activity, StartFightActivity::class.java)
                intent.putExtra("fightpoint_uuid", currentFightPoint?.uuid)
                intent.putExtra("ownerUsername", currentFightPoint?.user?.username)
                intent.putExtra("n_question", currentFightPoint?.n_questions)
                intent.putExtra("ownerScore", currentFightPoint?.score)
                startActivity(intent)
            }
        }
        return rootView
    }

    private var fightPoints: List<FightPoint>? = null

    @RequiresApi(Build.VERSION_CODES.N)
    private fun placeMarkers(googleMap: GoogleMap) {
        val apiInterface = ApiInterface.create().getFightPoints()

        apiInterface?.enqueue( object : Callback<List<FightPoint>> {

            override fun onResponse(call: Call<List<FightPoint>>, response: Response<List<FightPoint>>) {
                if(response.body() != null){
                    fightPoints = response.body()
                    for (i in fightPoints!!) {
                        if(i.user == null) {
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(i.posizione.toLatLng())
                                    .title("${i.city},${i.state}")
                                    .snippet("Not owned. Tap me to fight!")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                            )
                        } else {
                            if (i.user!!.firebase_id == MyApplication.instance.currentFirebaseUser?.uid) {
                                googleMap.addMarker(
                                    MarkerOptions()
                                        .position(i.posizione.toLatLng())
                                        .title("${i.city},${i.state}")
                                        .snippet("  You own this point!\n" +
                                                "Score: ${i.score}")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                )
                            } else {
                                googleMap.addMarker(
                                    MarkerOptions()
                                        .position(i.posizione.toLatLng())
                                        .title("${i.city},${i.state}")
                                        .snippet("Own by ${i.user!!.username}. Tap me to fight!\n" +
                                                "Score: ${i.score}")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                )
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<FightPoint>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun String.toLatLng(): LatLng {
        val split = this.split(", ")
        return LatLng(split[0].toDouble(), split[1].toDouble())
    }
}