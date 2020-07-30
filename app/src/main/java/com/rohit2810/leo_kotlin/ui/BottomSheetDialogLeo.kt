package com.rohit2810.leo_kotlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rohit2810.leo_kotlin.R

class BottomSheetDialogLeo : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v: View = inflater.inflate(R.layout.precautions_dialog, container, false)
        var textView = v.findViewById<TextView>(R.id.tv_Prec)
        var list = mutableListOf<String>()
        list.add("Do not wear headphones or be consumed with your cell phone unless you are in a familiar place or a secure area.")
        list.add("Do not be a soft target for a robbery or people that would bring harm to you.")
        list.add("Do not accept drinks gum cigars cigarettes or food from people that you are not familiar with.")
        list.add("Never leave keys under doormats, flowerpots, mailboxes or other secret hiding places -- burglars know where to look for hidden keys.")

        var text = ""
        var count = 1
        for(item in list) {
            text += "${count}. ${item}\n"
            count++
        }
        textView.text = text
        return v
    }
}