package com.practicum.playlistmaker.presentation.library

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R

class MediaLibraryActivity : AppCompatActivity() {

    private val imageUrl = "https://img.freepik.com/free-vector/open-blue-book-white_1308-69339.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_library)

        val searchButtonBack = findViewById<ImageView>(R.id.arrowBack)
        val image = findViewById<ImageView>(R.id.image)

        searchButtonBack.setOnClickListener{
            finish()
        }

        Glide.with(applicationContext)
            .load(imageUrl)
            .into(image)
    }
}
