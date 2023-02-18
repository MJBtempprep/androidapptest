package com.example.recyclersample.detailPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.recyclersample.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val authorImage: ImageView = findViewById(R.id.author_image)
        authorImage.setImageResource(R.drawable.pho)
    }
}