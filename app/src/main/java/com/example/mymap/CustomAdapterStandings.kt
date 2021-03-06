package com.example.mymap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CustomAdapterStandings(private val dataSet: ArrayList<StandingsRecord>) :
    RecyclerView.Adapter<CustomAdapterStandings.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgAvatar: ImageView
        val txtUser: TextView
        val txtScore: TextView
        val txtNumber: TextView

        init {
            txtUser = view.findViewById(R.id.txtTitleRecProfile)
            txtScore = view.findViewById(R.id.txtScoreRecProfile)
            txtNumber = view.findViewById(R.id.txtNumberRecProfile)
            imgAvatar = view.findViewById(R.id.imgStateRecProfile)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_rec_view_profile, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.txtNumber.text = "#${position + 4}"
        viewHolder.txtUser.text = dataSet[position].username
        viewHolder.txtScore.text = dataSet[position].score.toString()

        Picasso.get()
            .load("http://192.168.1.104:3000/img/avatar?id=" + dataSet[position].avatar)
            .into(viewHolder.imgAvatar, object: com.squareup.picasso.Callback {
                override fun onSuccess() {}
                override fun onError(e: java.lang.Exception?) {}
            })

    }

    override fun getItemCount() = dataSet.size
}
