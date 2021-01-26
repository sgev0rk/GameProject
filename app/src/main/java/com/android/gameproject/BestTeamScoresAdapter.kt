package com.android.gameproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.gameproject.databinding.RecyclerViewItemBinding

internal class BestTeamScoresAdapter(private var items: List<BestTeamScores>) :
    RecyclerView.Adapter<BestTeamScoresAdapter.BestTeamScoresViewHolder>() {

    fun clear() {
        items = listOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestTeamScoresViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false)
        return BestTeamScoresViewHolder(view)
    }

    override fun onBindViewHolder(holder: BestTeamScoresViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class BestTeamScoresViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = RecyclerViewItemBinding.bind(itemView)

        fun bind(leader: BestTeamScores) {
            binding.tvRecycleTeamName.text = leader.teamName
            binding.tvRecycleTeamScore.text = leader.score.toString()
        }
    }
}

