package com.example.mymap

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StartFightActivity: AppCompatActivity() {
    private lateinit var btnStart: Button
    private lateinit var txtTitleFight: TextView
    private var ownerScore: Int = 0
    private var ownerUsername: String = ""
    private var fightpoint_uuid: String = ""
    private var n_question: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_fight)

        btnStart = findViewById(R.id.btnStart)
        txtTitleFight = findViewById(R.id.txtTitleFight)

        fightpoint_uuid = intent.getStringExtra("fightpoint_uuid").toString()
        ownerUsername = intent.getStringExtra("ownerUsername").toString()
        n_question = intent.getIntExtra("n_questions", 0)
        ownerScore = intent.getIntExtra("ownerScore", 0)

        txtTitleFight.text = "You are going to fight vs $ownerUsername"

        btnStart.setOnClickListener {
            val intent = Intent(this, FightActivity::class.java)
            intent.putExtra("fightpoint_uuid", fightpoint_uuid)
            intent.putExtra("ownerUsername", ownerUsername)
            intent.putExtra("n_question", n_question)
            intent.putExtra("ownerScore", ownerScore)
            startActivity(intent)
            finish()
        }
    }
}