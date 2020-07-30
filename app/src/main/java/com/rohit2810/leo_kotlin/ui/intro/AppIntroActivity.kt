package com.rohit2810.leo_kotlin.ui.intro;

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import com.rohit2810.leo_kotlin.Application
import com.rohit2810.leo_kotlin.MainActivity
import com.rohit2810.leo_kotlin.R
import com.rohit2810.leo_kotlin.utils.getIntroPrefs
import com.rohit2810.leo_kotlin.utils.saveIntroPrefs

class AppIntroActivity : AppIntro() {

    var n = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getIntroPrefs(this)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        addSlide(AppIntroFragment.newInstance(
            title = "Welcome To Leo!",
            description = "Your safety is our responsibility",
            backgroundDrawable = R.drawable.back_slide6,
            imageDrawable = R.drawable.white_leo2
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "Power Button Detection",
            description = "Catching the trouble when the power button is pressed at least 5 times",
            imageDrawable = R.drawable.intro_power_button_detection,
            backgroundDrawable = R.drawable.back_slide2
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "Travel Safe",
            description = "Intuitive map of crime hotspots in your region.",
            imageDrawable = R.drawable.intro_travel_safe,
            backgroundDrawable = R.drawable.intro_wifi_p2p_back
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "Recent Crime News",
            description = "Get to know recent crime incidents in your region.",
            imageDrawable = R.drawable.intro_recent_crime_news,
            backgroundDrawable = R.drawable.back_slide3
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "Wifi P2P",
            description = "Trouble detection works seamlessly even when there is no cellular reception using WiFi P2P.",
            imageDrawable = R.drawable.intro_wifi_p2p,
            backgroundDrawable = R.drawable.back_slide5
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "Fall Detection",
            description = "Fall is detected based on analyzing acceleration patterns generated during various activities",
            imageDrawable = R.drawable.intro_fall_detection,
            backgroundDrawable = R.drawable.back_slide1
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "Quick Settings Tile",
            description = "Quick settings tile in system panel to easily notify when you are in trouble.",
            imageDrawable = R.drawable.intro_quick_settings_tool,
            backgroundDrawable = R.drawable.back_slide4
        ))
        setTransformer(AppIntroPageTransformerType.Parallax(
            titleParallaxFactor = 1.0,
            imageParallaxFactor = -1.0,
            descriptionParallaxFactor = 2.0
        ))
//        setTransformer(AppIntroPageTransformerType.Depth)
    }

    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        n = position
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        while (n < 6) {
            goToNextSlide()
        }
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        startActivity(Intent(this, MainActivity::class.java))
        saveIntroPrefs(this)
        Application.shouldShowSplash = false
        finish()
    }

}
