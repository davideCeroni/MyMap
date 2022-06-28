package com.example.mymap

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StandingsFragment: Fragment() {
    private lateinit var txtFirst: TextView
    private lateinit var txtSecond: TextView
    private lateinit var txtThird: TextView
    private lateinit var recList: RecyclerView
    private lateinit var itemsList : ArrayList<StandingsRecord>
    private lateinit var customAdapter: CustomAdapterStandings

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_standings, null) as ViewGroup
        txtFirst = root.findViewById(R.id.txtFirst)
        txtSecond = root.findViewById(R.id.txtSecond)
        txtThird = root.findViewById(R.id.txtThird)
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
                txtFirst.text = itemsList[0].username + ", " + itemsList[0].score.toString()
                itemsList.removeAt(0)
                txtSecond.text = ""
                txtThird.text =  ""
            }

            2-> {
                txtFirst.text = itemsList[0].username + ", " + itemsList[0].score.toString()
                itemsList.removeAt(0)
                txtSecond.text = itemsList[0].username + ", " + itemsList[0].score.toString()
                itemsList.removeAt(0)
                txtThird.text =  ""
            }

            else-> {
                txtFirst.text = itemsList[0].username + ", " + itemsList[0].score.toString()
                itemsList.removeAt(0)
                txtSecond.text = itemsList[0].username + ", " + itemsList[0].score.toString()
                itemsList.removeAt(0)
                txtThird.text = itemsList[0].username + ", " + itemsList[0].score.toString()
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