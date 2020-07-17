package com.rohit2810.leo_kotlin.ui.news

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rohit2810.leo_kotlin.R
import com.rohit2810.leo_kotlin.models.Article

class NewsAdapter internal constructor(
    val context: Context,
    private val clickListener : (Article) -> Unit
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {




    private var articles: List<Article> = emptyList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var newsImageView: ImageView = itemView.findViewById(R.id.news_item_imageview)
        var sourceTextView: TextView = itemView.findViewById(R.id.news_source_tv)
        var titleTextView: TextView = itemView.findViewById(R.id.news_item_title)

        fun bind(article : Article, clickListener: (Article) -> Unit, context: Context) {
            Glide.with(context).load(article!!.urlToImage).centerCrop().into(newsImageView)
//            sourceTextView.text = article.source.name
            titleTextView.text = article.title
            itemView.setOnClickListener{
                clickListener(article)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articles[position], clickListener, context)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    internal fun setArray(articles: List<Article>) {
        if (articles.isNotEmpty()) {
            this.articles = articles
        }
        notifyDataSetChanged()
    }


}