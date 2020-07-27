package com.rohit2810.leo_kotlin.ui.news

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rohit2810.leo_kotlin.R
import com.rohit2810.leo_kotlin.models.news.News

class NewsAdapter internal constructor(
    val context: Context,
    private val clickListener : (News) -> Unit
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private var news: List<News> = emptyList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var dateTextView: TextView = itemView.findViewById(R.id.news_item_date)
        var titleTextView: TextView = itemView.findViewById(R.id.news_item_title)
        var regionTextView: TextView = itemView.findViewById(R.id.news_item_region)
        var crimeTextView: TextView = itemView.findViewById(R.id.news_item_crime)
        fun bind(news : News, clickListener: (News) -> Unit, context: Context) {
//            dateTextView.text = news.date
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

    internal fun setArray(news: List<News>) {
        if (news.isNotEmpty()) {
            this.news = news
        }
        notifyDataSetChanged()
    }


}