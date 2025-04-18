package com.example.therecipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.therecipe.Homepage.HomepageActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Handler(Looper.getMainLooper())
            .postDelayed({
                startActivity(Intent(this, HomepageActivity::class.java))
                finish()
            }, 2000)
    }
}