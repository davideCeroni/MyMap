package com.example.mymap

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StandingsFragment: Fragment() {
    private lateinit var txtFirst: TextView
    private lateinit var imgFirst: ImageView
    private lateinit var txtFirstScore: TextView
    private lateinit var txtSecond: TextView
    private lateinit var imgSecond: ImageView
    private lateinit var txtSecondScore: TextView
    private lateinit var txtThird: TextView
    private lateinit var imgThird: ImageView
    private lateinit var txtThirdScore: TextView
    private lateinit var recList: RecyclerView
    private lateinit var itemsList : ArrayList<StandingsRecord>
    private lateinit var customAdapter: CustomAdapterStandings

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_standings, null) as ViewGroup
        txtFirst = root.findViewById(R.id.txtFirst)
        imgFirst = root.findViewById(R.id.imgAvatarGold)
        txtFirstScore = root.findViewById(R.id.txtScoreGold)
        txtSecond = root.findViewById(R.id.txtSecond)
        imgSecond = root.findViewById(R.id.imgAvatarSilver)
        txtSecondScore = root.findViewById(R.id.txtScoreSilver)
        imgThird = root.findViewById(R.id.imgAvatarBronze)
        txtThird = root.findViewById(R.id.txtThird)
        txtThirdScore = root.findViewById(R.id.txtScoreBronze)
        recList = root.findViewById(R.id.recListStandings)
        getStandings()
        return root
    }

    private fun loadStandings() {

        when(itemsList.size) {
            0-> {
                txtFirst.text = ""
                txtSecond.text = ""
                txtThird.text =  ""
            }

            1-> {
                txtFirst.text = itemsList[0].username
                txtFirstScore.text = itemsList[0].score.toString()
                Picasso.get()
                    .load("http://192.168.1.104:3000/img/avatar?id=" + itemsList[0].avatar)
                    .into(imgFirst, object: com.squareup.picasso.Callback {
                        override fun onSuccess() {}
                        override fun onError(e: java.lang.Exception?) {}
                    })
                itemsList.removeAt(0)

                txtSecond.text = ""

                txtThird.text =  ""
            }

            2-> {
                txtFirst.text = itemsList[0].username
                txtFirstScore.text = itemsList[0].score.toString()
                Picasso.get()
                    .load("http://192.168.1.104:3000/img/avatar?id=" + itemsList[0].avatar)
                    .into(imgFirst, object: com.squareup.picasso.Callback {
                        override fun onSuccess() {}
                        override fun onError(e: java.lang.Exception?) {}
                    })
                itemsList.removeAt(0)

                txtSecond.text = itemsList[0].username
                txtSecondScore.text = itemsList[0].score.toString()
                Picasso.get()
                    .load("http://192.168.1.104:3000/img/avatar?id=" + itemsList[0].avatar)
                    .into(imgSecond, object: com.squareup.picasso.Callback {
                        override fun onSuccess() {}
                        override fun onError(e: java.lang.Exception?) {}
                    })
                itemsList.removeAt(0)

                txtThird.text =  ""
            }

            else-> {
                txtFirst.text = itemsList[0].username
                txtFirstScore.text = itemsList[0].score.toString()
                Picasso.get()
                    .load("http://192.168.1.104:3000/img/avatar?id=" + itemsList[0].avatar)
                    .into(imgFirst, object: com.squareup.picasso.Callback {
                        override fun onSuccess() {}
                        override fun onError(e: java.lang.Exception?) {}
                    })
                itemsList.removeAt(0)

                txtSecond.text = itemsList[0].username
                txtSecondScore.text = itemsList[0].score.toString()
                Picasso.get()
                    .load("http://192.168.1.104:3000/img/avatar?id=" + itemsList[0].avatar)
                    .into(imgSecond, object: com.squareup.picasso.Callback {
                        override fun onSuccess() {}
                        override fun onError(e: java.lang.Exception?) {}
                    })
                itemsList.removeAt(0)

                txtThird.text = itemsList[0].username
                txtThirdScore.text = itemsList[0].score.toString()
                Picasso.get()
                    .load("http://192.168.1.104:3000/img/avatar?id=" + itemsList[0].avatar)
                    .into(imgThird, object: com.squareup.picasso.Callback {
                        override fun onSuccess() {}
                        override fun onError(e: java.lang.Exception?) {}
                    })
                itemsList.removeAt(0)
            }
        }

        if(itemsList.size > 0) {
            customAdapter = CustomAdapterStandings(itemsList)
            val layoutManager = LinearLayoutManager(activity!!)
            recList.layoutManager = layoutManager
            recList.adapter = customAdapter
            customAdapter.notifyDataSetChanged()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getStandings () {
        val apiInterface = ApiInterface.create().getStandings()

        apiInterface?.enqueue( object : Callback<ArrayList<StandingsRecord>> {

            override fun onResponse(call: Call<ArrayList<StandingsRecord>>, response: Response<ArrayList<StandingsRecord>>) {
                itemsList = response.body()!!
                loadStandings()
            }

            override fun onFailure(call: Call<ArrayList<StandingsRecord>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}