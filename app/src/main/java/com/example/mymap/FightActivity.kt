package com.example.mymap

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FightActivity: AppCompatActivity() {
    private lateinit var btnAnswer1: Button
    private lateinit var btnAnswer2: Button
    private lateinit var btnAnswer3: Button
    private lateinit var btnAnswer4: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var txtQuestion: TextView
    private lateinit var txtScore: TextView
    private lateinit var obj: ObjectAnimator
    private var currentProgress = 0
    private var duration: Int = 5000
    private var score = 0
    private var miss = 0
    private var ownerScore: Int = 0
    private var ownerUsername: String = ""
    private var fightpoint_uuid: String = ""
    private var n_question: Int = 0
    private lateinit var currentQuestionAnswers: QuestionAnswers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight)

        btnAnswer1 = findViewById(R.id.btnAnswer1)
        btnAnswer2 = findViewById(R.id.btnAnswer2)
        btnAnswer3 = findViewById(R.id.btnAnswer3)
        btnAnswer4 = findViewById(R.id.btnAnswer4)
        progressBar = findViewById(R.id.progressBar)
        txtQuestion = findViewById(R.id.txtQuestion)
        txtScore = findViewById(R.id.txtScore)

        fightpoint_uuid = intent.getStringExtra("fightpoint_uuid").toString()
        ownerUsername = intent.getStringExtra("ownerUsername").toString()
        n_question = intent.getIntExtra("n_questions", 0)
        ownerScore = intent.getIntExtra("ownerScore", 0)

        start()
    }

    private fun start() {
        newQuestion()

        progressBar.max = 1000
        currentProgress = 1000
        obj = ObjectAnimator.ofInt(progressBar, "progress", currentProgress)
        obj.setDuration(duration.toLong()).start()

        obj.doOnEnd {
            endGame()
        }

        btnAnswer1.setOnClickListener {
            answer(btnAnswer1)
        }

        btnAnswer2.setOnClickListener {
            answer(btnAnswer2)
        }

        btnAnswer3.setOnClickListener {
            answer(btnAnswer3)
        }

        btnAnswer4.setOnClickListener {
            answer(btnAnswer4)
        }
    }

    private fun answer(btn: Button) {
        if (btn.text == currentQuestionAnswers.correct_answer) {

            btn.setBackgroundColor(Color.GREEN)
            object : CountDownTimer(300, 50) {
                override fun onTick(arg0: Long) { }

                override fun onFinish() {
                    btn.setBackgroundColor(Color.WHITE)
                }
            }.start()

            score += 1
            txtScore.text = "Your score: $score"
            obj.setDuration(duration.toLong()).start()
            newQuestion()
        } else {
            miss += 1
            btn.setBackgroundColor(Color.RED)

            object : CountDownTimer(300, 50) {
                override fun onTick(arg0: Long) { }

                override fun onFinish() {
                    btn.setBackgroundColor(Color.WHITE)
                }
            }.start()

            if(miss >= 3) {
                obj.cancel()
                endGame()
            } else {
                obj.setDuration(duration.toLong()).start()
                newQuestion()
            }
        }
    }

    private fun endGame() {
        val intent = Intent(this, EndFightActivity::class.java)
        intent.putExtra("fightpoint_uuid", fightpoint_uuid)
        intent.putExtra("ownerUsername", ownerUsername)
        intent.putExtra("n_question", n_question)
        intent.putExtra("ownerScore", ownerScore)
        intent.putExtra("score", score)
        startActivity(intent)
    }

    private fun newQuestion() {
        val apiInterface = ApiInterface.create().getQuestionAnswers(fightpoint_uuid, (0..n_question).random())

        apiInterface.enqueue( object : Callback<QuestionAnswers> {

            override fun onResponse(call: Call<QuestionAnswers>, response: Response<QuestionAnswers>) {
                currentQuestionAnswers = response.body()!!
                txtQuestion.text = currentQuestionAnswers.question
                val rnd = (1..4).random()
                when (rnd) {
                    1-> {
                        btnAnswer1.text = currentQuestionAnswers.correct_answer
                        btnAnswer2.text = currentQuestionAnswers.wrong_answers[0]
                        btnAnswer3.text = currentQuestionAnswers.wrong_answers[1]
                        btnAnswer4.text = currentQuestionAnswers.wrong_answers[2]
                    }
                    2-> {
                        btnAnswer2.text = currentQuestionAnswers.correct_answer
                        btnAnswer1.text = currentQuestionAnswers.wrong_answers[0]
                        btnAnswer3.text = currentQuestionAnswers.wrong_answers[1]
                        btnAnswer4.text = currentQuestionAnswers.wrong_answers[2]
                    }
                    3-> {
                        btnAnswer3.text = currentQuestionAnswers.correct_answer
                        btnAnswer2.text = currentQuestionAnswers.wrong_answers[0]
                        btnAnswer1.text = currentQuestionAnswers.wrong_answers[1]
                        btnAnswer4.text = currentQuestionAnswers.wrong_answers[2]
                    }
                    4-> {
                        btnAnswer4.text = currentQuestionAnswers.correct_answer
                        btnAnswer2.text = currentQuestionAnswers.wrong_answers[0]
                        btnAnswer3.text = currentQuestionAnswers.wrong_answers[1]
                        btnAnswer1.text = currentQuestionAnswers.wrong_answers[2]
                    }
                }
            }

            override fun onFailure(call: Call<QuestionAnswers>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}