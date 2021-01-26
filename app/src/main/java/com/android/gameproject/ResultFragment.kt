package com.android.gameproject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.gameproject.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setListeners()
    }

    private fun setListeners() {
        binding.tvBestTeamScores.setOnClickListener {
            goToBestTeamScores()
        }
        binding.tvShare.setOnClickListener {
            shareGame()
        }
        binding.btnToMainScreen.setOnClickListener {
            goMainScreen()
        }
    }

    private fun goToBestTeamScores() {
        arguments?.getParcelable<GameData>(KEY)?.let {
            activity?.let { fragmentActivity ->
                val fragmentManager = fragmentActivity.supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.container, BestTeamScoresFragment())
                fragmentManager.popBackStack()
                fragmentTransaction.addToBackStack("")
                fragmentTransaction.commitAllowingStateLoss()
            }
        }
    }

    private fun goMainScreen() {
        activity?.let {
            it.supportFragmentManager.popBackStack()
        }
    }

    private fun shareGame() {
        arguments?.getParcelable<GameData>(KEY)?.let {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    getString(
                        R.string.share_message_string,
                        it.team1Name,
                        it.team2Name,
                        it.team1Result,
                        it.team2Result
                    )
                )
                type = "text/plain"
            }

            val shareIntent = Intent
                .createChooser(intent, getString(R.string.share_with_friends))
            startActivity(shareIntent)
        }
    }

    private fun setupViews() {
        arguments?.getParcelable<GameData>(KEY)?.let {
            binding.tvTeam1Name.text = it.team1Name
            binding.tvTeam2Name.text = it.team2Name
            binding.tvResultsScore.text =
                getString(R.string.score_board, it.team1Result, it.team2Result)
        }
    }

    companion object {
        private const val KEY = "dataKey"

        fun newInstance(data: GameData): ResultFragment {
            val resultFragment = ResultFragment()
            val bundle = Bundle()
            bundle.putParcelable(KEY, data)
            resultFragment.arguments = bundle
            return resultFragment
        }
    }
}
