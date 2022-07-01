package com.example.mymap

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
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
        val btnFight: Button = view.findViewById(R.id.btnFightAgain)
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
        //prendo il fightpoint dell'item corrente
        val fightpointPosition: Int = position - items.notifications.size
        lateinit var currentFightPoint: FightPoint
        if (fightpointPosition < 0){
            //notifica
            currentFightPoint = items.notifications[position].fightpoint
            viewHolder.txtLost.text = "You lost this state!"
        }
        else{
            currentFightPoint = items.fightpoints[fightpointPosition]
        }

        viewHolder.btnFight.setOnClickListener{
            val intent = Intent(context, StartFightActivity::class.java)
            intent.putExtra("fightpoint_uuid", currentFightPoint.uuid)
            intent.putExtra("ownerUsername", currentFightPoint.user?.username)
            intent.putExtra("n_question", currentFightPoint.n_questions)
            intent.putExtra("ownerScore", currentFightPoint.score)
            startActivity(context, intent, intent.extras)
        }


        viewHolder.txtTitle.text = "${currentFightPoint.state}, ${currentFightPoint.city}"

        Picasso.get()
            .load("http://192.168.1.104:3000/img/country?country=${currentFightPoint.state}")
            .into(viewHolder.imgState)

        if (currentFightPoint.user == null){
            //  fightpoint senza owner
            viewHolder.txtLost.text = ""
            viewHolder.avatar.setImageResource(0)
            viewHolder.txtSnippet.text = "Not Owned"
            return
        }
        //  fightpoint con owner
        viewHolder.txtSnippet.text = currentFightPoint.user?.username

        Picasso.get()
            .load("http://192.168.1.104:3000/img/avatar?id=${currentFightPoint.user!!.avatar}")
            .into(viewHolder.avatar)
    }
}
