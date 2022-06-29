package com.example.mymap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CustomAdapterProfile(private val dataSet: ArrayList<FightPoint>) :
    RecyclerView.Adapter<CustomAdapterProfile.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgState: ImageView
        val txtTitle: TextView

        init {
            txtTitle = view.findViewById(R.id.txtTitleRecProfile)
            imgState = view.findViewById(R.id.imgStateRecProfile)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_rec_view_profile, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.txtTitle.text = dataSet[position].city + ", " + dataSet[position].state

        val state = dataSet[position].state.lowercase().replace(' ', '_')

        Picasso.get()
            .load("http://192.168.1.104:3000/img/country?country=" + state)
            .into(viewHolder.imgState, object: com.squareup.picasso.Callback {
                override fun onSuccess() {}
                override fun onError(e: java.lang.Exception?) {}
            })
    }

    override fun getItemCount() = dataSet.size
}
