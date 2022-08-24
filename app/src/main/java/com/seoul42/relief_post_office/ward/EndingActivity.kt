package com.seoul42.relief_post_office.ward

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.seoul42.relief_post_office.R
import com.seoul42.relief_post_office.databinding.WardEndingBinding
import com.seoul42.relief_post_office.model.*
import com.seoul42.relief_post_office.viewmodel.FirebaseViewModel

class EndingActivity : AppCompatActivity() {

    private val binding: WardEndingBinding by lazy {
        WardEndingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 보이스 재생 후 종료
        val endingGuideVoice = MediaPlayer.create(this, R.raw.safetyending)

        endingGuideVoice.setOnCompletionListener {
            endingGuideVoice.release()
            finish()
        }
        endingGuideVoice.start()
        setStatusBarTransparent()
    }

    private fun setStatusBarTransparent() {
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}