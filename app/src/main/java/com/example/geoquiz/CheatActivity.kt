package com.example.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

const val EXTRA_ANSWER_SHOWN = "answer_shown"
const val EXTRA_CHEAT_COUNT = "cheat_count"
private const val EXTRA_ANSWER_IS_TRUE = "answer_is_true"
private const val ANSWER_KEY = "answer"

private var answerIsTrue = false
var cheatCounts = 1

class CheatActivity : AppCompatActivity() {

    private lateinit var warningTextView: TextView
    private lateinit var answerTextView: TextView
    private lateinit var apiVersionTextView: TextView
    private lateinit var showAnswerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        warningTextView = findViewById(R.id.warning_text)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        apiVersionTextView = findViewById(R.id.api_code_text_view)
        if (cheatCounts > 3) {
            showAnswerButton.isEnabled = false
        }

        if (!showAnswerButton.isEnabled) {
            warningTextView.text = resources.getString(R.string.cheating_limit_exceeded)
        }

        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
            setAnswerShownResult(true, cheatCounts++)
        }

        apiVersionTextView.text =
            resources.getString(R.string.api_level, Build.VERSION.SDK_INT.toString())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ANSWER_KEY, answerIsTrue.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        answerTextView.text = savedInstanceState.getString(ANSWER_KEY,"")
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean, cheatCounts: Int) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
            putExtra(EXTRA_CHEAT_COUNT, cheatCounts)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}