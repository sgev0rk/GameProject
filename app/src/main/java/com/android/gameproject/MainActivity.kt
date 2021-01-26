package com.android.gameproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

internal class MainActivity : AppCompatActivity() {

    var listOfBestTeamScores = mutableListOf<BestTeamScores>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newGameFragment = NewGameFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, newGameFragment)
            .addToBackStack("")
            .commitAllowingStateLoss()
    }
}
