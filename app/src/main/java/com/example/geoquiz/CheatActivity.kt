package com.example.geoquiz

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import org.jetbrains.anko.find

class CheatActivity : AppCompatActivity() {

    var isCheater = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        val showAnswer = find<Button>(R.id.show_answer)

        showAnswer.setOnClickListener {
            isCheater = true
            val intent = intent
            intent.putExtra("isCheater", true)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
