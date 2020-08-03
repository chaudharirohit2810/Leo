package com.rohit2810.leo_kotlin.ui.routes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginLeft
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rohit2810.leo_kotlin.R
import com.rohit2810.leo_kotlin.models.news.UnsafeNews
import timber.log.Timber

class BottomSheetDialogNews(var ele: UnsafeNews) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v: View = inflater.inflate(R.layout.precautions_dialog, container, false)
        var title = v.findViewById<TextView>(R.id.prec_tvTitle)
        var url = v.findViewById<TextView>(R.id.tv_Prec)
        title.text = ele.headline
        url.text = "Visit Page"
        url.setPadding(20, 0, 0, 0)
        url.textSize = 18f
        url.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        url.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ele.url))
            startActivity(intent)
        }
        return v
    }
}