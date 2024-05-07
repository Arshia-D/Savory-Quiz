package com.example.quiz_app

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class PlayerFragment : Fragment() {

    private lateinit var playersRecyclerView: RecyclerView
    private lateinit var adapter: PlayerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player, container, false)

        val players = listOf(
            Player(1, "Player 1", Color.RED),
            Player(2, "Player 2", Color.BLUE),
            Player(3, "Player 3", Color.parseColor("#A020F0")),
            Player(4, "Player 4", Color.GREEN)
        )

        playersRecyclerView = view.findViewById(R.id.playersRecyclerView)
        adapter = PlayerAdapter(players) { colorName ->
            showDialog("Oh I See You Like  $colorName")
        }
        playersRecyclerView.adapter = adapter
        playersRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("OK") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }
}