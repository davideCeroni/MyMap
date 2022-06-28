package com.example.mymap

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso

class CustomInfoWindowForGoogleMap(context: Context): GoogleMap.InfoWindowAdapter {
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.info_window, null)

    private fun rendowWindowText(marker: Marker, view: View){

        val tvTitle = view.findViewById<TextView>(R.id.txtTitleInfoWindow)
        val tvSnippet = view.findViewById<TextView>(R.id.txtSnippet)
        val tvImage = view.findViewById<ImageView>(R.id.imgStateInfoWindow)

        tvTitle.text = marker.title
        tvSnippet.text = marker.snippet

        val cityState = tvTitle.text.split(',')

        Picasso.get()
            .load("http://192.168.1.104:3000/img/country?country=" + cityState[1])
            .into(tvImage, object: com.squareup.picasso.Callback {
                override fun onSuccess() {
                    if (marker.isInfoWindowShown) {
                        marker.hideInfoWindow()
                        marker.showInfoWindow()
                    }
                }
                override fun onError(e: java.lang.Exception?) {}
            })
    }

    override fun getInfoContents(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }
}