package com.Seoul42.relief_post_office

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.Seoul42.relief_post_office.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}