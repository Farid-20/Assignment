package com.example.newsapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class NewsDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        // Retrieve the passed data from the Intent
        val title = intent.getStringExtra("title") ?: "No Title"
        val description = intent.getStringExtra("description") ?: "No Description"
        val imageUrl = intent.getStringExtra("imageUrl") ?: ""  // Provide a default value if null

        // Set the title and description
        val titleTextView: TextView = findViewById(R.id.news_title)
        val descriptionTextView: TextView = findViewById(R.id.news_description)
        val imageView: ImageView = findViewById(R.id.ivNewsDetailImage)

        // Set the text fields to the received data
        titleTextView.text = title
        descriptionTextView.text = description

        // Load the image using Picasso (or Glide)
        Picasso.get().load(imageUrl).into(imageView)
    }
}
