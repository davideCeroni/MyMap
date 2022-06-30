package com.example.mymap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import okhttp3.internal.notify

class CarouselAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var items: CarouselInfo

    class ItemCarousel(view: View) : RecyclerView.ViewHolder(view) {
        val imgState: ImageView = view.findViewById(R.id.iconCarousel)
        val avatar: ImageView = view.findViewById(R.id.avatarCarousel)
        val txtLost: TextView = view.findViewById(R.id.txtLost)
        val txtTitle: TextView = view.findViewById(R.id.txtTitleCarousel)
        val txtSnippet: TextView = view.findViewById(R.id.txtSnippetCarousel)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_carousel
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(viewType, viewGroup, false)

        return ItemCarousel(view)
    }

    override fun getItemCount() = items.notifications.size + items.fightpoints.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        viewHolder as ItemCarousel
        val fightpointPosition: Int = position - items.notifications.size

        if (fightpointPosition < 0){
            //notifica
            val notifica: Notification = items.notifications[position]
            viewHolder.txtTitle.text = "${notifica.fightpoint.state}, ${notifica.fightpoint.city}"
            viewHolder.txtSnippet.text = notifica.fightpoint.user?.username
            viewHolder.txtLost.text = "You lost this state!"

            Picasso.get()
                .load("http://192.168.1.104:3000/img/country?country=${notifica.fightpoint.state}")
                .into(viewHolder.imgState)

            Picasso.get()
                .load("http://192.168.1.104:3000/img/avatar?id=${notifica.fightpoint.user!!.avatar}")
                .into(viewHolder.avatar)

            return
        }
        //  fightpoint
        val fightpoint:FightPoint = items.fightpoints[fightpointPosition]

        viewHolder.txtTitle.text = "${fightpoint.state}, ${fightpoint.city}"

        Picasso.get()
            .load("http://192.168.1.104:3000/img/country?country=${fightpoint.state}")
            .into(viewHolder.imgState)

        if (fightpoint.user == null){
            //  fightpoint senza owner
            viewHolder.txtSnippet.text = "Not Owned"
            return
        }
        //  fightpoint con owner
        viewHolder.txtSnippet.text = fightpoint.user?.username

        Picasso.get()
            .load("http://192.168.1.104:3000/img/avatar?id=${fightpoint.user!!.avatar}")
            .into(viewHolder.avatar)
    }
}
