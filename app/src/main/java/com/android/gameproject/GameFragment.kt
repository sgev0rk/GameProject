package com.android.gameproject

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.android.gameproject.databinding.FragmentGameBinding

internal class GameFragment : Fragment() {

    private lateinit var timer: CountDownTimer
    private lateinit var binding: FragmentGameBinding

    private var team1Score: Int = 0
    private var team2Score: Int = 0
    private var duration: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = arguments
        arguments?.getParcelable<GameData>(KEY)?.let {
            duration = it.durationInMills
            setTexts(it)
        }
        updateScoreBoard()
        setListeners()
        startTimer()
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
    }

    private fun setListeners() {
        binding.tvIncreaseTeamOne.setOnClickListener {
            team1Score++
            updateScoreBoard()
        }
        binding.tvIncreaseTeamTwo.setOnClickListener {
            team2Score++
            updateScoreBoard()
        }
        binding.tvCancel.setOnClickListener {
            cancelGame()
        }
        binding.tvFinish.setOnClickListener {
            finishGame()
        }
    }

    private fun finishGame() {
        timer.cancel()

        arguments?.getParcelable<GameData>(KEY)?.let {
            val gameData = GameData(
                it.team1Name,
                it.team2Name,
                it.durationInMills,
                team1Score,
                team2Score
            )
            val resultFragment = ResultFragment.newInstance(gameData)

            activity?.let { fragmentActivity ->
                saveToLeaderBoard(fragmentActivity, gameData)

                val fragmentManager = fragmentActivity.supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.container, resultFragment)

                fragmentManager.popBackStack()

                fragmentTransaction.addToBackStack("")
                fragmentTransaction.commitAllowingStateLoss()
            }
        }
    }

    //Only save to leaderboard teams that win
    private fun saveToLeaderBoard(
        fragmentActivity: FragmentActivity,
        gameData: GameData
    ) {
        val activity = fragmentActivity as MainActivity
        val bestScores = activity.listOfBestTeamScores
        if (bestScores.isEmpty()) {
            if (gameData.team1Result > gameData.team2Result) {
                addFistTeamToLeaderBoard(bestScores, gameData)
            } else if (gameData.team1Result < gameData.team2Result) {
                addSecondTeamToLeaderBoard(bestScores, gameData)
            }


        } else {
            bestScores.map { team ->
                if (team.teamName == gameData.team1Name && gameData.team1Result > gameData.team2Result) {
                    team.score = team.score + gameData.team1Result

                }
                if (team.teamName == gameData.team2Name && gameData.team1Result < gameData.team2Result) {
                    team.score = team.score + gameData.team2Result
                }
            }

        }

        if (gameData.team1Result > gameData.team2Result) {
            addFistTeamToLeaderBoard(bestScores, gameData)
        }
        if (gameData.team1Result < gameData.team2Result) {
            addSecondTeamToLeaderBoard(bestScores, gameData)
        }
    }

    private fun addSecondTeamToLeaderBoard(
        bestScores: MutableList<BestTeamScores>,
        gameData: GameData
    ) {
        bestScores.add(
            BestTeamScores(
                gameData.team2Name,
                gameData.team2Result
            )
        )
    }

    private fun addFistTeamToLeaderBoard(
        bestScores: MutableList<BestTeamScores>,
        gameData: GameData
    ) {
        bestScores.add(
            BestTeamScores(
                gameData.team1Name,
                gameData.team1Result
            )
        )
    }

    private fun cancelGame() {
        timer.cancel()
        activity?.let {
            it.supportFragmentManager.popBackStack()
        }
    }

    private fun setTexts(data: GameData) {
        binding.tvTeamOne.text = data.team1Name
        binding.tvTeamTwo.text = data.team2Name
        binding.tvTimer.text = convertMilsToMinutesAndSeconds(data.durationInMills)
    }

    private fun convertMilsToMinutesAndSeconds(duration: Long): String {
        val minutes = (duration / MILLISECONDS_IN_MINUTE)
            .toString().padStart(2, '0')
        val seconds = (duration % MILLISECONDS_IN_MINUTE / MILLISECONDS_IN_SECOND)
            .toString().padStart(2, '0')

        return requireActivity().getString(R.string.timer_text, minutes, seconds)
    }

    private fun updateScoreBoard() {
        binding.tvScoreBoard.text =
            getString(
                R.string.score_board,
                team1Score,
                team2Score
            )
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            duration,
            MILLISECONDS_IN_SECOND
        ) {
            override fun onTick(timeLeft: Long) {
                duration = timeLeft
                binding.tvTimer.text =
                    convertMilsToMinutesAndSeconds(timeLeft)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer.start()
    }

    companion object {
        private const val MILLISECONDS_IN_SECOND: Long = 1000
        private const val MILLISECONDS_IN_MINUTE: Long = 60000
        private const val KEY = "dataKey"

        fun newInstance(data: GameData): GameFragment {
            val gameFragment = GameFragment()
            val bundle = Bundle()
            bundle.putParcelable(KEY, data)
            gameFragment.arguments = bundle
            return gameFragment
        }
    }
}
