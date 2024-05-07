package com.example.quiz_app

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

data class Player(val id: Int, val name: String, val color: Int)

class PlayerAdapter(private val players: List<Player>, private val itemClickListener: (String) -> Unit) :
    RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    class PlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val button: Button = view.findViewById(R.id.playerButton)

        fun bind(player: Player, clickListener: (String) -> Unit) {
            button.text = player.name
            button.backgroundTintList = ColorStateList.valueOf(player.color)
            button.setOnClickListener {
                val colorName = when (player.color) {
                    Color.RED -> "Red"
                    Color.BLUE -> "Blue"
                    Color.parseColor("#A020F0") -> "Purple"
                    Color.GREEN -> "Green"
                    else -> "Unknown Color"
                }
                clickListener(colorName)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_player, parent, false)
        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(players[position], itemClickListener)
    }

    override fun getItemCount(): Int = players.size
}
