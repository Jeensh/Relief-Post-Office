package com.Seoul42.relief_post_office

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.Seoul42.relief_post_office.databinding.ActivityResultBinding
import com.bumptech.glide.Glide
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ResultActivity : AppCompatActivity() {
    val binding by lazy { ActivityResultBinding.inflate(layoutInflater) }
    val database = Firebase.database
    val storage = Firebase.storage("gs://relief-post-office-58784.appspot.com")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //intent로 넘어와야 할 정보들
        val wardId = "userid-5"
        //끝
        database.getReference("users")
            .child(wardId)
            .child("name")
            .get()
            .addOnSuccessListener {
            binding.textWardName.text = it.value.toString()
        }
        downloadImage("/profile/${wardId}.png")
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