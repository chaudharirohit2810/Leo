package com.rohit2810.leo_kotlin.ui.intro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import com.rohit2810.leo_kotlin.R

class IntroViewPageAdapter(
    var context: Context,
    var mListScreen: MutableList<ScreenItem>
) : PagerAdapter(){


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var layoutScreen: View = inflater.inflate(R.layout.layout_screen, null)
        layoutScreen.background = ContextCompat.getDrawable(context, mListScreen.get(position).backgroundDrawable)

        var imgSlide = layoutScreen.findViewById<ImageView>(R.id.intro_img)
        var title = layoutScreen.findViewById<TextView>(R.id.intro_title)
        var description = layoutScreen.findViewById<TextView>(R.id.intro_description)

        title.text = mListScreen.get(position).title
        description.text = mListScreen.get(position).description
        imgSlide.setImageResource(mListScreen.get(position).imageDrawable)

        container.addView(layoutScreen)
        return layoutScreen
    }
    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view == o
    }

    override fun getCount(): Int {
        return mListScreen.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}