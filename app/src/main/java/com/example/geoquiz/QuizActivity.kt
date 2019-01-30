package com.example.geoquiz

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.geoquiz.model.Question
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.*

class QuizActivity : AppCompatActivity() {

    private val question: Array<Question> = arrayOf(
        Question(R.string.first_statement, true),
        Question(R.string.second_statement, true),
        Question(R.string.third_statement, false),
        Question(R.string.fourth_statement, false),
        Question(R.string.fifth_statement, true),
        Question(R.string.sixth_statement, true)
    )

    private var checkExistAnswer: ArrayList<Boolean> = ArrayList()

    private var currentIndex: Int = 0
    private lateinit var statementText: TextView
    private val TAG = "QuizActivity"
    private val KEY_INDEX = "index"
    private var countTrueAnswer = 0
    private var countAnswer = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        Log.d(TAG, "onCreate(Bundle) called")

        if (savedInstanceState != null)
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0)

        for (i in 0 until question.size)
            checkExistAnswer.add(false)

        val trueButton = find<Button>(R.id.yes_button)
        val falseButton = find<Button>(R.id.no_button)
        val nextButton = find<ImageButton>(R.id.next_button)
        val prevButton = find<ImageButton>(R.id.prev_button)
        statementText = find(R.id.statement)

        statementText.setText(question[currentIndex].textResID)

        trueButton.setOnClickListener {
            if (countAnswer >= question.size)
                trueButton.isClickable = false
            checkAnswer(true)
        }
        falseButton.setOnClickListener {
            if (countAnswer >= question.size)
                falseButton.isClickable = false
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            if (countAnswer != question.size - 1)
                updateQuestion("+")
        }

        prevButton.setOnClickListener {
            if (countAnswer != question.size - 1)
                updateQuestion("-")
        }

        statementText.setOnClickListener {
            updateQuestion("+")
        }
    }

    private fun updateQuestion(string: String) {
        if (countAnswer >= question.size - 1)
            toast("It's a last question")
        when (string) {
            "+" -> do {
                currentIndex = (currentIndex + 1) % question.size
            } while (checkExistAnswer[currentIndex])
            "-" -> do {
                if (currentIndex == 0)
                    currentIndex = question.size
                currentIndex = (currentIndex - 1) % question.size
            } while (checkExistAnswer[currentIndex])
            else -> toast("LOL                                                                                                                            ")
        }
        statementText.setText(question[currentIndex].textResID)
    }

    private fun checkNumberAnswer(): Boolean {
        for (i in 0 until checkExistAnswer.size)
            if (!checkExistAnswer[i])
                return false
        return true
    }

    private fun checkAnswer(answer: Boolean) {
        countAnswer++
        if (answer == question[currentIndex].answerTrue) {
            toast("Correct!!!")
            countTrueAnswer++
        } else toast("Incorrect(")
        checkExistAnswer[currentIndex] = true

        if (checkNumberAnswer()) {
            toast("That's all!")
            val result = countTrueAnswer.toFloat() / question.size.toFloat() * 100
            this.finish()
            startActivity<MainActivity>("result" to result)
        } else
            updateQuestion("+")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop called")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState?.putInt(KEY_INDEX, currentIndex)
    }
}
