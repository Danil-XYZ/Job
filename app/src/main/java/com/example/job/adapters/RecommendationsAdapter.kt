package com.example.job.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.job.R
import com.example.job.databinding.ItemRecommendationsBinding
import com.example.job.model.Offer

class RecommendationsAdapter(
    private var dataList: List<Offer>
) : RecyclerView.Adapter<RecommendationsAdapter.ViewHolder>() {

    fun updateData(newDataList: List<Offer>) {
        dataList = newDataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecommendationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  ViewHolder(binding)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    class ViewHolder(binding: ItemRecommendationsBinding) : RecyclerView.ViewHolder(binding.root) {

        val binding = binding

        fun bind(data: Offer) = with(binding) {

            title.setText(data.title)
            greenTextView.setText(data.button?.text ?: "")
            when(data.id) {
                "near_vacancies" -> {
                    icon.setBackgroundResource(R.drawable.blue_round_icon)
                    icon.setImageResource(R.drawable.ic_near_vacancies)
                }
                "level_up_resume" ->{
                    icon.setBackgroundResource(R.drawable.green_round_icon)
                    icon.setImageResource(R.drawable.ic_level_up_resume)
                }
                "temporary_job" ->{
                    icon.setBackgroundResource(R.drawable.green_round_icon)
                    icon.setImageResource(R.drawable.ic_temporary_job)
                }
            }
        }
    }
}