package com.seoul42.relief_post_office.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.seoul42.relief_post_office.R
import com.seoul42.relief_post_office.databinding.ItemResultDetailBinding
import com.seoul42.relief_post_office.model.AnswerDTO
import com.seoul42.relief_post_office.result.ResultQuestionDetailActivity

class ResultDetailAdapter (private val context : Context,
                           private val answerList: MutableList<Pair<String, AnswerDTO>>,
                           private val safetyName: String,
                           private val answerDate: String)
    : RecyclerView.Adapter<ResultDetailAdapter.ResultDetailHolder>() {
    inner class ResultDetailHolder(private val binding: ItemResultDetailBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("NotifyDataSetChanged", "ResourceAsColor")
        fun setQuestionAnswer(answer: Pair<String, AnswerDTO>) {
            setQuestionText(binding, answer.second)
            setAnswerRecord(binding, answer.second)
            setAnswerReply(binding, answer.second)
            binding.textResultQuetion.setOnClickListener {
                val intent = Intent(context, ResultQuestionDetailActivity::class.java)
                intent.putExtra("safetyName", safetyName)
                intent.putExtra("answer", answer)
                intent.putExtra("answerDate", answerDate)
                startActivity(context, intent, null)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultDetailHolder {
        val binding = ItemResultDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ResultDetailHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultDetailHolder, position: Int) {
        val answer = answerList.get(position)
        holder.setQuestionAnswer(answer)
    }

    override fun getItemCount(): Int {
        return answerList.size
    }

    private fun setQuestionText(binding: ItemResultDetailBinding, answer: AnswerDTO) {
        binding.textResultQuetion.text = answer.questionText
    }

    private fun setAnswerRecord(binding: ItemResultDetailBinding, answer: AnswerDTO) {
        val recordBtn = binding.btnResultQuetionPlay
        if (answer.questionRecord && (answer.answerSrc != "") && (answer.reply == true)) {
            recordBtn.setImageResource(R.drawable.playbtn)
            recordBtn.visibility = View.VISIBLE
            // 재생 버튼 클릭 이벤트
            var playing = false
            var player: MediaPlayer? = null
            recordBtn.setOnClickListener{
                // 재생 중이면 재생 버튼으로 이미지 변경
                if (playing){
                    player?.release()
                    player = null
                    recordBtn.setImageResource(R.drawable.playbtn)
                    playing = false
                }
                // 재생 중이 아니면 중지 버튼으로 이미지 변경
                else{
                    // 녹음 소스 불러와서 미디어 플레이어 세팅
                    player = MediaPlayer().apply {
                        setDataSource(answer.answerSrc)
                        prepare()
                    }

                    player?.setOnCompletionListener {
                        player?.release()
                        player = null

                        recordBtn.setImageResource(R.drawable.playbtn)
                        playing = false
                    }

                    // 재생
                    player?.start()

                    recordBtn.setImageResource(R.drawable.stopbtn)
                    playing = true
                }
            }
        }
        else
            recordBtn.visibility = View.INVISIBLE
    }

    private fun setAnswerReply(binding: ItemResultDetailBinding, answer: AnswerDTO) {
        val replyImg = binding.imgResultAnswer
        if (answer.reply == true)
            replyImg.setBackgroundResource(R.drawable.answer_positive)
        else
            replyImg.setBackgroundResource(R.drawable.answer_negative)
    }

}