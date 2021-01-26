package com.android.gameproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.gameproject.databinding.FragmentNewGameBinding
import com.google.android.material.textfield.TextInputLayout

internal class NewGameFragment : Fragment() {

    private lateinit var binding: FragmentNewGameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding.btnStart.setOnClickListener {
            startGame()
        }
        binding.btnBestTeamScores.setOnClickListener {
            goToBestTeamScores()
        }
    }

    private fun startGame() {
        if (teamNamesIsCorrect(
                binding.tilTeam1Name,
                binding.tilTeam2Name
            ) && timeIsCorrect(binding.tilTime)
        ) {
            activity?.let {

                val gameData = GameData(
                    binding.tietTeam1Name.text.toString(),
                    binding.tietTeam2Name.text.toString(),
                    binding.tietTime.text.toString().toLong() * 1000
                )
                val newGameFragment = GameFragment.newInstance(gameData)
                it.supportFragmentManager.beginTransaction()
                    .add(R.id.container, newGameFragment)
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    }

    private fun goToBestTeamScores() {
        activity?.let { fragmentActivity ->
            val fragmentManager = fragmentActivity.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.container, BestTeamScoresFragment())
            fragmentTransaction.addToBackStack("")
            fragmentTransaction.commitAllowingStateLoss()
        }
    }

    private fun teamNamesIsCorrect(
        ilTeam1Name: TextInputLayout,
        ilTeam2Name: TextInputLayout
    ): Boolean {
        ilTeam1Name.error = null
        ilTeam2Name.error = null
        var teamNamesIsCorrect = true
        val team1Name = ilTeam1Name.editText?.text.toString()
        val team2Name = ilTeam2Name.editText?.text.toString()

        if (team1Name.isBlank()) {
            ilTeam1Name.error = getString(R.string.error_et_text)
            teamNamesIsCorrect = false
        }
        if (team2Name.isBlank()) {
            ilTeam2Name.error = getString(R.string.error_et_text)
            teamNamesIsCorrect = false
        }
        if (teamNamesIsCorrect && team1Name == team2Name) {
            teamNamesIsCorrect = false
            ilTeam1Name.error = getString(R.string.same_team_names)
            ilTeam2Name.error = getString(R.string.same_team_names)
        }
        return teamNamesIsCorrect
    }

    private fun timeIsCorrect(inputLayout: TextInputLayout): Boolean {
        inputLayout.error = null
        var timeIsCorrect = true
        val time = inputLayout.editText?.text.toString()

        if (time.isBlank()) {
            inputLayout.error = getString(R.string.error_et_text)
            timeIsCorrect = false
        }
        try {
            time.toLong()
        } catch (e: NumberFormatException) {
            inputLayout.error = getString(R.string.wrong_number_format)
            timeIsCorrect = false
        }
        return timeIsCorrect
    }
}

