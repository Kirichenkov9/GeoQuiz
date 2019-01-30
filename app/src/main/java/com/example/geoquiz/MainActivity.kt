package com.example.geoquiz

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    private var result: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = intent
        result = intent.getFloatExtra("result", 0F)

        val textResult = find<TextView>(R.id.result)
        val startGame = find<Button>(R.id.start_game)

        if (result != 0F)
            textResult.text = "Your result: $result%"

        startGame.setOnClickListener {
            startActivity<QuizActivity> ()
        }
    }
}
