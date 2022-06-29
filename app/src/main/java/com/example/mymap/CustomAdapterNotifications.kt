package com.example.mymap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CustomAdapterNotifications(private val dataSet: ArrayList<Notification>) :
    RecyclerView.Adapter<CustomAdapterNotifications.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgState: ImageView
        val txtTitle: TextView
        val txtSnippet: TextView
        val txtDateTime: TextView

        init {
            txtTitle = view.findViewById(R.id.txtTitleRecNotifications)
            txtSnippet = view.findViewById(R.id.txtSnippetRecNotifications)
            txtDateTime = view.findViewById(R.id.txtDateTimeRecNotifications)
            imgState = view.findViewById(R.id.imgStateRecNotifications)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_rec_view_notifications, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.txtTitle.text = dataSet[position].fightpoint.city + ", " + dataSet[position].fightpoint.state
        viewHolder.txtSnippet.text = dataSet[position].fightpoint.user!!.username + " beat your record and the new one is: " + dataSet[position].fightpoint.score.toString()
        viewHolder.txtDateTime.text = getDate(dataSet[position].dateTime)

        val state = dataSet[position].fightpoint.state.lowercase().replace(' ', '_')
        Picasso.get()
            .load("http://192.168.1.104:3000/img/country?country=" + state)
            .into(viewHolder.imgState, object: com.squareup.picasso.Callback {
                override fun onSuccess() {}
                override fun onError(e: java.lang.Exception?) {}
            })
    }

    override fun getItemCount() = dataSet.size

    private fun getDate(str: String): String {
        var finalString = ""
        finalString += str[8]
        finalString += str[9]
        finalString += "/"
        finalString += str[5]
        finalString += str[6]
        finalString += "/"
        finalString += str[0]
        finalString += str[1]
        finalString += str[2]
        finalString += str[3]
        finalString += " at "
        finalString += str[11]
        finalString += str[12]
        finalString += str[13]
        finalString += str[14]
        finalString += str[15]
        return finalString
    }
}
