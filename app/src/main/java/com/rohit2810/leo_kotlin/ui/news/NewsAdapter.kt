package com.rohit2810.leo_kotlin.ui.news

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rohit2810.leo_kotlin.R
import com.rohit2810.leo_kotlin.models.news.News
import com.rohit2810.leo_kotlin.models.news.NewsDatabaseModule
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter internal constructor(
    val context: Context,
    private val clickListener : (NewsDatabaseModule) -> Unit
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private var news: List<NewsDatabaseModule> = emptyList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var dateTextView: TextView = itemView.findViewById(R.id.news_item_date)
        var titleTextView: TextView = itemView.findViewById(R.id.news_item_title)
        var regionTextView: TextView = itemView.findViewById(R.id.news_item_region)
        var crimeTextView: TextView = itemView.findViewById(R.id.news_item_crime)
        @SuppressLint("SimpleDateFormat")
        fun bind(news : NewsDatabaseModule, clickListener: (NewsDatabaseModule) -> Unit, context: Context) {
            val date = Date(news.date)
            val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
            dateTextView.text = format.format(date)
            if (news.date == 0L) {
                dateTextView.visibility = View.INVISIBLE
            }
            titleTextView.text = news.headline
            regionTextView.text = news.city
            crimeTextView.text = news.crime
            itemView.setOnClickListener{
                clickListener(news)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(news[position], clickListener, context)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    internal fun setArray(news: List<NewsDatabaseModule>) {
        if (news.isNotEmpty()) {
            this.news = news
        }
        notifyDataSetChanged()
    }


}