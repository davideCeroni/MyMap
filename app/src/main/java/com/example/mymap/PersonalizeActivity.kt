package com.example.mymap

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.widget.*
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
    private lateinit var btnSave: Button
    private var nickSendable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalization)

        rbtnGroup = findViewById(R.id.rbtnGroup)
        val initAvatar = intent.getIntExtra("avatar", 1)
        val b:RadioButton = rbtnGroup.getChildAt(initAvatar-1) as RadioButton
        b.isChecked = true
        txtTypeUsername = findViewById(R.id.txtTypeUsername)
        editTextUsername = findViewById(R.id.edittextUsername)
        btnSave = findViewById(R.id.btnSave)

        editTextUsername.text = SpannableStringBuilder(MyApplication.instance.currentFirebaseUser!!.displayName.toString())

        editTextUsername.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) { }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                nickSendable = if(s.length < 3 || s.length > 13){
                    editTextUsername.setTextColor(Color.parseColor("#cf0a2b"))
                    false
                } else {
                    editTextUsername.setTextColor(Color.parseColor("#5de310"))
                    true
                }
            }
        })

        btnSave.setOnClickListener {
            if (nickSendable) userExist(this)
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
    private fun updateUser(username: String, avatar: Int) {
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
                val radioButton: RadioButton = findViewById(rbtnGroup.checkedRadioButtonId)
                if (errorMessage.message == "user not found") {
                    createUser(editTextUsername.text.toString(), radioButton.text.toString().toInt(), thisContext)
                } else {
                    updateUser(editTextUsername.text.toString(), radioButton.text.toString().toInt())
                }
            }

            override fun onFailure(call: Call<ErrorMessage>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}