package com.android.gameproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.gameproject.databinding.FragmentBestTeamScoresBinding

class BestTeamScoresFragment : Fragment() {

    private lateinit var binding: FragmentBestTeamScoresBinding
    private lateinit var bestTeamScoresAdapter: BestTeamScoresAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBestTeamScoresBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setListeners()
    }

    private fun setListeners() {
        binding.tvNewGame.setOnClickListener {
            goToNewGame()
        }
        binding.ibClear.setOnClickListener {
            reset()
        }
    }

    private fun setupRecycler() {
        activity?.let { fragmentActivity ->
            val activity = fragmentActivity as MainActivity
            binding.rvTeamScores.apply {
                val list = activity.listOfBestTeamScores
                list.sortByDescending { it.score }
                bestTeamScoresAdapter = BestTeamScoresAdapter(list)
                adapter = bestTeamScoresAdapter
            }
        }
    }

    private fun reset() {
        activity?.let { fragmentActivity ->
            val activity = fragmentActivity as MainActivity
            activity.listOfBestTeamScores = arrayListOf()
            bestTeamScoresAdapter.clear()
        }
    }

    private fun goToNewGame() {
        activity?.let {
            it.supportFragmentManager.popBackStack()
        }
    }
}
