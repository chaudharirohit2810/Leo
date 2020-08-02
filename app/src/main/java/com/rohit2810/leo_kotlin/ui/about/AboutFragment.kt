package com.rohit2810.leo_kotlin.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.rohit2810.leo_kotlin.R
import java.net.URLEncoder


class AboutFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = "About Us"
        var view = inflater.inflate(R.layout.fragment_about, container, false)
        var tv_website = view.findViewById<TextView>(R.id.tv_website)
        var tv_bot = view.findViewById<TextView>(R.id.tv_bot)

        tv_website.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://powerful-plateau-47954.herokuapp.com/"))
            startActivity(intent)
        }

        tv_bot.setOnClickListener {
            val packageManager = requireContext()!!.packageManager
            val i = Intent(Intent.ACTION_VIEW)

            try {
                val url =
                    "https://api.whatsapp.com/send?phone=" + "+1 (415) 523-8886" + "&text=" + URLEncoder.encode(
                        "join calm-social",
                        "UTF-8"
                    )
                i.setPackage("com.whatsapp")
                i.data = Uri.parse(url)
                if (i.resolveActivity(packageManager) != null) {
                    requireContext()!!.startActivity(i)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return view
    }
}