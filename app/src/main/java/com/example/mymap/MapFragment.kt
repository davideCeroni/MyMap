package com.example.mymap

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapFragment : Fragment() {

    /*private val images = listOf(
        R.drawable.avatar1,
        R.drawable.avatar2,
        R.drawable.avatar3,
        R.drawable.avatar4
    )*/

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_map, container, false)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment!!.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity!!, R.raw.map_style))
            mMap.uiSettings.isMapToolbarEnabled = false
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

        //val viewPager = initialiseViewPager()

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
                                    .icon(bitmapDescriptorFromVector(activity!!, R.drawable.ic_ping_blue))
                            )
                        } else {
                            if (i.user!!.firebase_id == MyApplication.instance.currentFirebaseUser?.uid) {
                                googleMap.addMarker(
                                    MarkerOptions()
                                        .position(i.posizione.toLatLng())
                                        .title("${i.city},${i.state}")
                                        .snippet("  You own this point!\n" +
                                                "Score: ${i.score}")
                                        .icon(bitmapDescriptorFromVector(activity!!, R.drawable.ic_ping_green))
                                )
                            } else {
                                googleMap.addMarker(
                                    MarkerOptions()
                                        .position(i.posizione.toLatLng())
                                        .title("${i.city},${i.state}")
                                        .snippet("Own by ${i.user!!.username}. Tap me to fight!\n" +
                                                "Score: ${i.score}")
                                        .icon(bitmapDescriptorFromVector(activity!!, R.drawable.ic_ping_red))
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

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    /*private fun initialiseViewPager() = rootView.findViewById<ViewPager2>(R.id.carouselViewPager).apply {
        adapter = CarouselAdapter(context).apply {
            items = images
            notifyDataSetChanged()
        }
    }*/
}