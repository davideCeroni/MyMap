package com.example.mymap

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonalizeActivity: AppCompatActivity() {

    private lateinit var rbtnGroup: RadioGroup
    private lateinit var txtTypeUsername: TextView
    private lateinit var editTextUsername: EditText
    private lateinit var btnSelect: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalization)

        rbtnGroup = findViewById(R.id.rbtnGroup)
        txtTypeUsername = findViewById(R.id.txtTypeUsername)
        editTextUsername = findViewById(R.id.edittextUsername)
        btnSelect = findViewById(R.id.btnSelect)

        editTextUsername.text = SpannableStringBuilder(MyApplication.instance.currentFirebaseUser!!.displayName.toString())

        btnSelect.setOnClickListener() {
            userExist(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createUser(username: String, avatar: Int, thisContext: Context) {
        val apiInterface = ApiInterface.create().createUser(User("", username, avatar))

        apiInterface.enqueue( object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val intent = Intent (thisContext, MainActivity::class.java)
                startActivity(intent)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateUser(username: String, avatar: Int, thisContext: Context) {
        val apiInterface = ApiInterface.create().updateUser(User("", username, avatar))

        apiInterface.enqueue( object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                finish()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun userExist (thisContext: Context) {
        val apiInterface = ApiInterface.create().getUsers()

        apiInterface?.enqueue( object : Callback<ErrorMessage> {

            override fun onResponse(call: Call<ErrorMessage>, response: Response<ErrorMessage>) {
                val errorMessage = response.body()!!
                if (errorMessage.message == "user not found") {
                    createUser(editTextUsername.text.toString(), rbtnGroup.checkedRadioButtonId, thisContext)
                } else {
                    updateUser(editTextUsername.text.toString(), rbtnGroup.checkedRadioButtonId, thisContext)
                }
            }

            override fun onFailure(call: Call<ErrorMessage>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}

