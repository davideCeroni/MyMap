package com.example.mymap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapterStandings(private val dataSet: ArrayList<StandingsRecord>) :
    RecyclerView.Adapter<CustomAdapterStandings.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTitle: TextView

        init {
            txtTitle = view.findViewById(R.id.txtTitleRecProfile)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_rec_view_profile, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.txtTitle.text = dataSet[position].username + ", " + dataSet[position].score.toString()
    }

    override fun getItemCount() = dataSet.size
}
