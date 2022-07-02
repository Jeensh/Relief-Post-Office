package com.Seoul42.relief_post_office

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.Seoul42.relief_post_office.databinding.ActivityResultBinding
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ResultActivity : AppCompatActivity() {
    val binding by lazy { ActivityResultBinding.inflate(layoutInflater) }
    val storage = Firebase.storage("gs://relief-post-office-58784.appspot.com")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        downloadImage("/profile/userid-1.png")
        setContentView(binding.root)
    }
    fun downloadImage(path: String) {
        storage.getReference(path).downloadUrl.addOnSuccessListener { uri ->
            Glide.with(this).load(uri).into(binding.imgProfile)
        }.addOnFailureListener {
            Log.e("스토리지", "다운로드 에러=>${it.message}")
        }
    }
}