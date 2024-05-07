package com.example.quiz_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tabs)
        val startGameButton = findViewById<Button>(R.id.startGameButton)
        val selectPlayerButton = findViewById<Button>(R.id.selectPlayerButton)
        val aboutMeButton = findViewById<Button>(R.id.aboutMeButton)

        // Setup the ViewPager adapter
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        // Set button listeners to change ViewPager page
        startGameButton.setOnClickListener { viewPager.currentItem = 0 }
        selectPlayerButton.setOnClickListener { viewPager.currentItem = 1 }
        aboutMeButton.setOnClickListener { viewPager.currentItem = 2 }

        // Setting up the TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Start Game"
                1 -> "Select Player"
                2 -> "Settings"
                else -> null
            }
        }.attach()
    }
}