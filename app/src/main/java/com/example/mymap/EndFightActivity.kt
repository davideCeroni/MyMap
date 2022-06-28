package com.example.mymap

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EndFightActivity: AppCompatActivity() {
    private lateinit var btnOk: Button
    private lateinit var txtYourScore: TextView
    private lateinit var txtOwnerScore: TextView
    private lateinit var txtWin: TextView
    private var score: Int = 0
    private var ownerScore: Int = 0
    private var ownerUsername: String = ""
    private var fightpoint_uuid: String = ""
    private var n_question: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_fight)

        btnOk = findViewById(R.id.btnOk)
        txtYourScore = findViewById(R.id.txtYourScore)
        txtOwnerScore = findViewById(R.id.txtOwnerScore)
        txtWin = findViewById(R.id.txtWin)

        fightpoint_uuid = intent.getStringExtra("fightpoint_uuid").toString()
        ownerUsername = intent.getStringExtra("ownerUsername").toString()
        n_question = intent.getIntExtra("n_questions", 0)
        ownerScore = intent.getIntExtra("ownerScore", 0)
        score = intent.getIntExtra("score", 0)

        txtYourScore.text = "Your score: $score"
        txtOwnerScore.text = "Owner score: $ownerScore"

        if(score > ownerScore) {
            txtWin.text = "YOU WON"
        } else {
            if(score == ownerScore) {
                txtWin.text = "TIE!"
            } else {
                txtWin.text = "YOU LOST!"
            }
        }

        btnOk.setOnClickListener {
            updateOwner()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateOwner() {
        val apiInterface = ApiInterface.create().updateOwner(UpdateOwnerObj(fightpoint_uuid, score))

        apiInterface.enqueue( object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val resp = response.body()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}