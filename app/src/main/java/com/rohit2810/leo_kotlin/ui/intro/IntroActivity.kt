package com.rohit2810.leo_kotlin.ui.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.google.android.material.tabs.TabLayout
import com.rohit2810.leo_kotlin.Application
import com.rohit2810.leo_kotlin.MainActivity
import com.rohit2810.leo_kotlin.R
import com.rohit2810.leo_kotlin.utils.getIntroPrefs
import com.rohit2810.leo_kotlin.utils.saveIntroPrefs
import kotlinx.android.synthetic.main.fragment_intro.*

class IntroActivity : AppCompatActivity() {
    private lateinit var btnAnim: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        if (getIntroPrefs(this)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        setContentView(R.layout.fragment_intro)
        btnAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.button_animation)
        supportActionBar?.hide()
        val list = mutableListOf<ScreenItem>()
        list.add(
            ScreenItem(
                "Leo",
                "Your saviour in trouble",
                R.drawable.leo2
            )
        )
        list.add(
            ScreenItem(
                "Fall Detection",
                "Fall is detected based on analyzing acceleration patterns generated during various activities",
                R.drawable.leo2
            )
        )
        list.add(
            ScreenItem(
                "Power Button Detection",
                "Catching the trouble when the power button is pressed at least 5 times",
                R.drawable.leo2
            )
        )
        list.add(
            ScreenItem(
                "Scream Detection",
                "Catching the trouble when the power button is pressed at least 5 times",
                R.drawable.leo2
            )
        )
        list.add(
            ScreenItem(
                "Wifi P2P",
                "Catching the trouble when the power button is pressed at least 5 times",
                R.drawable.leo2
            )
        )

        val introViewPageAdapter = IntroViewPageAdapter(this, list)
        screen_viewpager.adapter = introViewPageAdapter

        tab_indicator.setupWithViewPager(screen_viewpager)

        btn_next.setOnClickListener {
            var position = screen_viewpager.currentItem
            if (position < list.size) {
                position++
                screen_viewpager.currentItem = position
            }
            if (position == list.size - 1) {
                loadLastScreen()
            }
        }

        tab_indicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == list.size - 1) {
                    loadLastScreen()
                }
            }
        })

        btn_get_started.setOnClickListener {
            // TODO: Move to Login Page
            startActivity(Intent(this, MainActivity::class.java))
            saveIntroPrefs(this)
            Application.shouldShowSplash = false
            finish()
        }

        tv_skip.setOnClickListener {
            screen_viewpager.currentItem = list.size
        }
    }

    fun loadLastScreen() {
        btn_next.visibility = View.INVISIBLE
        btn_get_started.visibility = View.VISIBLE
        tv_skip.visibility = View.INVISIBLE
        tab_indicator.visibility = View.INVISIBLE

        //TODO: Setup Button Animation
        btn_get_started.animation = btnAnim
    }
}