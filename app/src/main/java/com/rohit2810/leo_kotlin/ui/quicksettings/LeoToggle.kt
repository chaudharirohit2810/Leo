package com.rohit2810.leo_kotlin.ui.quicksettings

import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.service.quicksettings.TileService
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.rohit2810.leo_kotlin.R
import com.rohit2810.leo_kotlin.models.user.User
import com.rohit2810.leo_kotlin.repository.TroubleRepository
import com.rohit2810.leo_kotlin.utils.getUserFromCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


@RequiresApi(Build.VERSION_CODES.N)
class LeoToggle: TileService() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onTileAdded() {
        super.onTileAdded()

    }

    override fun onTileRemoved() {
        super.onTileRemoved()
    }

    override fun onClick() {
        super.onClick()
        try {
            var coroutineScope = CoroutineScope(Dispatchers.Main)
            var user : User = getUserFromCache(this@LeoToggle) ?: throw Exception("Nothing in cache")
            coroutineScope.launch {
                try {
                    TroubleRepository.getInstance(this@LeoToggle).markTrouble(user)
                    showDialog(showSuccessPrompt())
                }catch (e: Exception) {

                    showDialog(showNotLoggedInPrompt("No internet connection!! Trying to connect to peers"))
                }
            }
        }catch (e: Exception) {
            Timber.d(e.localizedMessage)
            showDialog(showNotLoggedInPrompt("Please login to mark trouble"))
        }
    }

    private fun showSuccessPrompt(): Dialog {
        val textView = TextView(this)
        textView.text = "Success!!"
        textView.setPadding(60, 30, 20, 30)
        textView.textSize = 20f
        textView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        textView.setTextColor(Color.WHITE)

        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setCustomTitle(textView)
        builder.setIcon(R.drawable.leo2)
        builder.setMessage("You've marked yourself in trouble. Help is on the way!!")
        builder.setPositiveButton("Ok"
        ) { dialog, which -> dialog?.dismiss() }
        return builder.create()
    }

    private fun showNotLoggedInPrompt( msg: String) : Dialog {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        val textView = TextView(this)
        textView.text = "Error!!"
        textView.setPadding(80, 30, 20, 30)
        textView.textSize = 20f
        textView.setBackgroundColor(Color.parseColor("#ee6352"))
        textView.setTextColor(Color.WHITE)
        builder.setCustomTitle(textView)
        builder.setIcon(R.drawable.leo2)
        builder.setMessage(msg)
        builder.setPositiveButton("Ok"
        ) { dialog, which -> dialog?.dismiss() }

        return builder.create()

    }
}