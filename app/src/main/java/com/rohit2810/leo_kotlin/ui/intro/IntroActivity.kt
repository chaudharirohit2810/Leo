package com.rohit2810.leo_kotlin.ui.intro

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.rohit2810.leo_kotlin.Application
import com.rohit2810.leo_kotlin.MainActivity
import com.rohit2810.leo_kotlin.R
import com.rohit2810.leo_kotlin.ui.intro.ParallaxPageTransformer.ParallaxTransformInformation
import com.rohit2810.leo_kotlin.ui.intro.ParallaxPageTransformer.ParallaxTransformInformation.Companion.PARALLAX_EFFECT_DEFAULT
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
                title = "Welcome To Leo!",
                description = "Protecting you like family",
                backgroundDrawable = R.drawable.back_slide6,
                imageDrawable = R.drawable.white_leo2
            )
        )
        list.add(
            ScreenItem(
                title = "Power Button Detection",
                description = "Catching the trouble when the power button is pressed at least 5 times",
                imageDrawable = R.drawable.intro_power_button_detection,
                backgroundDrawable = R.drawable.back_slide2
            )
        )
        list.add(
            ScreenItem(
                title = "Travel Safe",
                description = "Intuitive map of crime hotspots in your region.",
                imageDrawable = R.drawable.intro_travel_safe,
                backgroundDrawable = R.drawable.intro_wifi_p2p_back
            )
        )
        list.add(
            ScreenItem(
                title = "Recent Crime News",
                description = "Get to know recent crime incidents in your region.",
                imageDrawable = R.drawable.intro_recent_crime_news,
                backgroundDrawable = R.drawable.back_slide3
            )
        )
        list.add(
            ScreenItem(
                title = "Wifi P2P",
                description = "Trouble detection works seamlessly even when there is no cellular reception using WiFi P2P.",
                imageDrawable = R.drawable.intro_wifi_p2p,
                backgroundDrawable = R.drawable.back_slide5
            )
        )
        list.add(
            ScreenItem(
                title = "Fall Detection",
                description = "Fall is detected based on analyzing acceleration patterns generated during various activities",
                imageDrawable = R.drawable.intro_fall_detection,
                backgroundDrawable = R.drawable.back_slide1
            )
        )
        list.add(
            ScreenItem(
                title = "Quick Settings Tile",
                description = "Quick settings tile in system panel to easily notify when you are in trouble.",
                imageDrawable = R.drawable.intro_quick_settings_tool,
                backgroundDrawable = R.drawable.back_slide4
            )
        )

        val pageTransformer = ParallaxPageTransformer()
            .addViewToParallax(ParallaxTransformInformation(R.id.intro_img, -1.0f, -1.0f))
            .addViewToParallax(
                ParallaxTransformInformation(
                    R.id.intro_title, 1.0f,
                    1.0f
                )
            ).addViewToParallax(ParallaxTransformInformation(R.id.intro_description, 2.0f, 2.0f))

        val introViewPageAdapter = IntroViewPageAdapter(this, list)
        screen_viewpager.adapter = introViewPageAdapter
        screen_viewpager.setPageTransformer(true, pageTransformer)

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

        btn_get_started.animation = btnAnim
    }
}