package com.seoul42.relief_post_office.result

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.seoul42.relief_post_office.adapter.ResultDetailAdapter
import com.seoul42.relief_post_office.databinding.ActivityResultDetailBinding
import com.seoul42.relief_post_office.model.AnswerDTO
import com.seoul42.relief_post_office.model.ResultDTO

class ResultDetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityResultDetailBinding.inflate(layoutInflater) }
    private val auth : FirebaseAuth by lazy { Firebase.auth }
    private val database = Firebase.database
    private var answerList = mutableListOf<Pair<String, AnswerDTO>>()
    private lateinit var adapter: ResultDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val wardId = intent.getSerializableExtra("wardId") as String
        val resultId = intent.getSerializableExtra("resultId") as String
        val result = intent.getSerializableExtra("result") as ResultDTO
        setSafetyName(result.safetyName)
        setWardName(wardId)
        setDate(result.date)
        setAdapter()
        setQuestionAnswerList(resultId)
    }

    private fun setSafetyName(safetyName: String) {
        binding.textResultDetailSafetyName.text = safetyName
    }

    private fun setWardName(wardId: String) {
        var wardName: String? = null
        database.getReference("user").child(wardId).child("name")
            .get().addOnSuccessListener {
            if (it.value != null) {
                wardName = it.value.toString()
            }
        }
        binding.textResultDetailWardName.text = wardName
    }

    private fun setDate(date: String) {
        binding.textResultDetailDate.text = date.replace("-", "/")

    }

    private fun setAdapter() {
        adapter = ResultDetailAdapter(this, answerList)
        with(binding) {
            resultDetailRecyclerView.adapter = adapter
            resultDetailRecyclerView.layoutManager = LinearLayoutManager(baseContext)
        }
    }

    private fun setQuestionAnswerList(resultId: String) {
        val answerListRef = database.getReference("result").child(resultId).child("answerList")

        answerListRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                questionAnswerList(resultId)
            }

            override fun onCancelled(error: DatabaseError) {
                print(error.message)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun questionAnswerList(resultId: String) {
        answerList.clear()
        adapter.notifyDataSetChanged()
        val answerListRef = database.getReference("result").child(resultId).child("answerIdList")
        val answerRef = database.getReference("answer")
        answerListRef.get().addOnSuccessListener {
            if (it.value != null) {
                val answerIdList = it.getValue<MutableMap<String, String>>() as MutableMap<String, String>
                for ((dummy, answerId) in answerIdList) {
                    answerRef.child(answerId).get().addOnSuccessListener {
                        if (it.value != null) {
                            val answer = it.getValue(AnswerDTO::class.java) as AnswerDTO
                            val userId = auth.uid.toString()
                            if (!answer.questionSecret) {
                                answerList.add(Pair(answerId, answer))
                                adapter.notifyDataSetChanged()
                            }
                            else if ((answer.questionSecret) and (answer.questionOwner == userId)) {
                                answerList.add(Pair(answerId, answer))
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        }
    }
}