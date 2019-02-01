package com.example.geoquiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.geoquiz.model.Question
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar

class QuizActivity : AppCompatActivity() {

    private val question: Array<Question> = arrayOf(
        Question(R.string.first_statement, true),
        Question(R.string.second_statement, true),
        Question(R.string.third_statement, false),
        Question(R.string.fourth_statement, false),
        Question(R.string.fifth_statement, true),
        Question(R.string.sixth_statement, true)
    )

    private var checkExistAnswer: ArrayList<Int> = arrayListOf()

    private var currentIndex: Int = 0
    private lateinit var statementText: TextView
    private val TAG = "QuizActivity"
    private val KEY_INDEX = "index"
    private val KEY_EXISTS_ANSWER = "exists_answer"
    private val KEY_TRUE_ANSWER = "true_answer"
    private val KEY_CHEATS = "cheats"
    private val KEY_ANSWER = "answer"
    private val numberCheats = "Your cheats: "
    private var countTrueAnswer: Int = 0
    private var countAnswer = 0
    private var countCheat = 3
    private lateinit var cheats: TextView
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        Log.d(TAG, "onCreate(Bundle) called")

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
            countAnswer = savedInstanceState.getInt(KEY_ANSWER, 0)
            countTrueAnswer = savedInstanceState.getInt(KEY_TRUE_ANSWER, 0)
            countCheat = savedInstanceState.getInt(KEY_CHEATS, 0)
        }

        Log.d("LOL", checkExistAnswer.toString())
        for (i in 0 until question.size)
            checkExistAnswer.add(0)

        trueButton = find<Button>(R.id.yes_button)
        falseButton = find<Button>(R.id.no_button)
        val nextButton = find<ImageButton>(R.id.next_button)
        val prevButton = find<ImageButton>(R.id.prev_button)
        val showAnswer = find<Button>(R.id.get_answer)
        cheats = find<TextView>(R.id.count_cheats)

        statementText = find(R.id.statement)

        statementText.setText(question[currentIndex].textResID)
        cheats.text = numberCheats + countCheat.toString()

        trueButton.setOnClickListener {
            trueButton.isClickable = false
            falseButton.isClickable = false

            launch {
                delay(1000)
                trueButton.isClickable = true
                falseButton.isClickable = true
            }
            checkAnswer(true)
        }
        falseButton.setOnClickListener {
            trueButton.isClickable = false
            falseButton.isClickable = false

            launch {
                delay(1000)
                trueButton.isClickable = true
                falseButton.isClickable = true
            }
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            updateQuestion("+")

        }

        prevButton.setOnClickListener {
            updateQuestion("-")
        }

        statementText.setOnClickListener {
            updateQuestion("+")
        }

        showAnswer.setOnClickListener {
            if (countCheat == 0)
                toast("You spent all cheats!")
            else
                startActivityForResult<CheatActivity>(1, "answer" to question[currentIndex].answerTrue)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null)
                if (data.getBooleanExtra("isCheater", false)) {
                    countCheat--
                    cheats.text = numberCheats + countCheat.toString()
                    toast(question[currentIndex].answerTrue.toString())
                }
        } else
            toast("LOL")
    }

    private fun updateQuestion(string: String) {
        when {
            countAnswer >= question.size -> toast("It's a last question")
            string == "+" -> do {
                currentIndex = (currentIndex + 1) % question.size
            } while (checkExistAnswer[currentIndex] == 1)
            string == "-" -> do {
                if (currentIndex == 0)
                    currentIndex = question.size
                currentIndex = (currentIndex - 1) % question.size
            } while (checkExistAnswer[currentIndex] == 1)
            else -> toast("LOL")
        }
        statementText.setText(question[currentIndex].textResID)
    }

    private fun checkNumberAnswer(): Boolean {
        for (i in 0 until checkExistAnswer.size)
            if (checkExistAnswer[i] == 0)
                return false
        return true
    }

    private fun checkAnswer(answer: Boolean) {
        countAnswer++
        checkExistAnswer[currentIndex] = 1

        if (answer == question[currentIndex].answerTrue) {
            toast("Correct!!!")
            countTrueAnswer++
        } else toast("Incorrect(")

        if (checkNumberAnswer()) {
            toast("That's all!")
            val result = countTrueAnswer.toFloat() / question.size.toFloat() * 100
            startActivity<MainActivity>("result" to result)
            finish()
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
        outState?.putInt(KEY_ANSWER, countAnswer)
        outState?.putInt(KEY_TRUE_ANSWER, countTrueAnswer)
        outState?.putInt(KEY_CHEATS, countCheat)
        outState?.putIntegerArrayList(KEY_EXISTS_ANSWER, checkExistAnswer)
    }
}