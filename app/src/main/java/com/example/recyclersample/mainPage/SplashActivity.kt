package com.example.recyclersample.mainPage


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.recyclersample.R


class SplashActivity: AppCompatActivity() {
    private val loading=3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        Handler().postDelayed({
            val home = Intent(this@SplashActivity, PersonListActivity::class.java)
            startActivity(home)
            finish()
        }, loading.toLong())
    }
}