package com.example.quiz_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.net.Uri
import android.widget.SeekBar
import android.media.MediaPlayer
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat

class AboutFragment : Fragment() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var seekBarVolume: SeekBar
    private lateinit var switchMusic: SwitchCompat
    private lateinit var btnRemoveAds: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        seekBarVolume = view.findViewById(R.id.seekBarVolume)
        switchMusic = view.findViewById(R.id.switchMusic)
        btnRemoveAds = view.findViewById(R.id.btnRemoveAds)

// play background music
        initializeMediaPlayer()

        switchMusic.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mediaPlayer?.start()
            } else {
                mediaPlayer?.pause()
            }
        }

        seekBarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val volume = progress / 100f
                    mediaPlayer?.setVolume(volume, volume)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        btnRemoveAds.setOnClickListener {
            openYouTubeLink()
        }

        return view
    }

    private fun openYouTubeLink() {
        val youtubeLink = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
        startActivity(intent)
    }

    private fun initializeMediaPlayer() {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setOnPreparedListener {
            // Optionally handle prepared event
            mediaPlayer?.start()
        }
        mediaPlayer?.setOnErrorListener { _, what, _ ->
            showAlert("Error $what occurred while initializing MediaPlayer")
            false
        }
        try {
            val assetFileDescriptor = requireContext().resources.openRawResourceFd(R.raw.music)
            mediaPlayer?.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)
            assetFileDescriptor.close()
            mediaPlayer?.prepareAsync()
            mediaPlayer?.isLooping = true
            mediaPlayer?.setVolume(1.0f, 1.0f)
        } catch (e: Exception) {
            showAlert("Exception occurred: ${e.message}")
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }


    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}