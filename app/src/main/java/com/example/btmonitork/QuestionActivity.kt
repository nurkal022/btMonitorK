package com.example.btmonitork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible


class QuestionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)


        setupQuestionToggle(R.id.question1, R.id.answer1)
        setupQuestionToggle(R.id.question2, R.id.answer2)
        setupQuestionToggle(R.id.question3, R.id.answer3)
        setupQuestionToggle(R.id.question4, R.id.answer4)
        setupQuestionToggle(R.id.question5, R.id.answer5)
    }

    private fun setupQuestionToggle(questionId: Int, answerId: Int) {
        val questionView = findViewById<TextView>(questionId)
        val answerView = findViewById<TextView>(answerId)

        questionView.setOnClickListener {
            answerView.visibility = if (answerView.visibility == View.GONE) View.VISIBLE else View.GONE
        }
    }
}