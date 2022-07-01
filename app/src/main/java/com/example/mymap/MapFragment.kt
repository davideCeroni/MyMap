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
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class MapFragment : Fragment() {

    private lateinit var rootView: View
    private var currentUser: UserInfo? = null
    private var fightPoints: List<FightPoint>? = null
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getUserInfo()
        rootView = inflater.inflate(R.layout.fragment_map, container, false)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment!!.getMapAsync { mMap ->
            map = mMap
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
        return rootView
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun placeMarkers(googleMap: GoogleMap) {
        val apiInterface = ApiInterface.create().getFightPoints()

        apiInterface?.enqueue( object : Callback<List<FightPoint>> {

            override fun onResponse(call: Call<List<FightPoint>>, response: Response<List<FightPoint>>) {
                if(response.body() == null) return
                fightPoints = response.body()
                for (i in fightPoints!!) {
                    if(i.user == null) {
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(i.posizione.toLatLng())
                                .title("${i.city},${i.state}")
                                .snippet("Not owned. Tap me to fight!")
                                .icon(bitmapDescriptorFromVector(activity!!, R.drawable.ic_pin_blue))
                        )
                    } else {
                        if (i.user!!.firebase_id == MyApplication.instance.currentFirebaseUser?.uid) {
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(i.posizione.toLatLng())
                                    .title("${i.city},${i.state}")
                                    .snippet("  You own this point!\n" +
                                            "Score: ${i.score}")
                                    .icon(bitmapDescriptorFromVector(activity!!, R.drawable.ic_pin_green))
                            )
                        } else {
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(i.posizione.toLatLng())
                                    .title("${i.city},${i.state}")
                                    .snippet("Own by ${i.user!!.username}. Tap me to fight!\n" +
                                            "Score: ${i.score}")
                                    .icon(bitmapDescriptorFromVector(activity!!, R.drawable.ic_pin_red))
                            )
                        }
                    }
                }
                checkData()
            }

            override fun onFailure(call: Call<List<FightPoint>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getUserInfo () {
        val apiInterface = ApiInterface.create().getUserInfo()

        apiInterface?.enqueue( object : Callback<UserInfo> {

            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                currentUser = response.body()!!
                checkData()
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
    private fun moveCameraOnMarkSelected(pos :String){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos.toLatLng(), 5F))

    }

    private fun checkData(){
        if (fightPoints != null && currentUser != null){
            val carousel = CarouselInfo(currentUser!!.notifications, fightPoints!!)

            val viewPager = initialiseViewPager(carousel)

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(position:Int, positionOffset:Float, positionOffsetPixels:Int) {
                    val pos = (position + positionOffset).roundToInt()
                    val fightpointPosition: Int = pos - carousel.notifications.size

                    if (fightpointPosition < 0) {
                        moveCameraOnMarkSelected(carousel.notifications[pos].fightpoint.posizione)
                    } else {
                        moveCameraOnMarkSelected(carousel.fightpoints[fightpointPosition].posizione)
                    }
                }
            })
        }
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

    private fun initialiseViewPager(carouselInfo: CarouselInfo) = rootView.findViewById<ViewPager2>(R.id.carouselViewPager).apply {
        adapter = CarouselAdapter(context).apply {
            items = carouselInfo
            notifyDataSetChanged()
        }
    }
}