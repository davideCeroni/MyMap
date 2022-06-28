package com.example.mymap

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment: Fragment() {
    private lateinit var recList: RecyclerView
    private lateinit var itemsList: ArrayList<FightPoint>
    private lateinit var customAdapter: CustomAdapterProfile
    private lateinit var imgProfile: ImageView
    private lateinit var txtUsername: TextView
    private lateinit var currentUser: UserInfo
    private lateinit var btnEdit: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_profile, null) as ViewGroup
        recList = root.findViewById(R.id.recListProfile)
        imgProfile = root.findViewById(R.id.imgProfile)
        txtUsername = root.findViewById(R.id.txtUsername)
        btnEdit = root.findViewById(R.id.btnEdit)

        getUserInfo()

        btnEdit.setOnClickListener() {
            val intent = Intent (activity!!, PersonalizeActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    private fun loadUsers() {

        txtUsername.text = currentUser.username

        Picasso.get()
            .load("http://192.168.1.104:3000/img/avatar?id=" + currentUser.avatar)
            .into(imgProfile)

        itemsList = currentUser.fightpointsOwned
        customAdapter = CustomAdapterProfile(itemsList)
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
                loadUsers()
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}