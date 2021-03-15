package com.nmrc.note

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class StartApp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_app)
        supportActionBar?.hide()

    }
}