package com.Seoul42.relief_post_office

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.Seoul42.relief_post_office.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding by lazy { ActivityResultBinding.inflate(layoutInflater) }
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}