package com.seoul42.relief_post_office

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.seoul42.relief_post_office.databinding.ItemResultBinding
import com.seoul42.relief_post_office.model.ResultDTO
import com.seoul42.relief_post_office.result.ResultDetailActivity

class ResultAdapter(private val context : Context,
                    private val resultList: MutableList<Pair<String, ResultDTO>>,
                    private val wardId: String)
    : RecyclerView.Adapter<ResultAdapter.ResultHolder>() {
    inner class ResultHolder(private val binding: ItemResultBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("NotifyDataSetChanged", "ResourceAsColor")
        fun setResult(result: Pair<String, ResultDTO>, context: Context) {
            val database = Firebase.database
            val safetyRef = database.getReference("safety")
            binding.itemResultSafetyName.text = result.second.safetyName
            binding.itemResultAlarmTime.text = result.second.safetyTime
            binding.itemResultResponseTime.text = result.second.responseTime
            if (!isResponsed(result.second.responseTime)) {
                binding.itemResultSafetyLayout.setBackgroundResource(R.drawable.result_disable_background)
                binding.itemResultResponseTime.setTextColor(R.color.red)
            }
            else {
                binding.itemResultSafetyLayout.setOnClickListener {
                    val intent = Intent(context, ResultDetailActivity::class.java)
                    intent.putExtra("wardId", wardId)
                    intent.putExtra("resultId", result.first)
                    intent.putExtra("result", result.second)
                    ContextCompat.startActivity(context, intent, null)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ResultHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultHolder, position: Int) {
        val result = resultList.get(position)
        holder.setResult(result, context)
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    private fun isResponsed(responseTime: String): Boolean {
        return responseTime != "미응답"
    }
}