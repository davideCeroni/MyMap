package com.example.mymap

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsFragment: Fragment() {
    private lateinit var recList: RecyclerView
    private lateinit var itemsList: ArrayList<Notification>
    private lateinit var customAdapter: CustomAdapterNotifications
    private lateinit var currentUser: UserInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_notifications, null) as ViewGroup
        recList = root.findViewById(R.id.recListNotifications)
        getUserInfo()
        return root
    }

    private fun loadStates() {
        itemsList = currentUser.notifications
        customAdapter = CustomAdapterNotifications(itemsList)
        val layoutManager = LinearLayoutManager(activity!!)
        recList.layoutManager = layoutManager
        recList.adapter = customAdapter
        customAdapter.notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getUserInfo () {
        val apiInterface = ApiInterface.create().getUserInfo()

        apiInterface?.enqueue( object : Callback<UserInfo> {

            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                currentUser = response.body()!!
                loadStates()
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}