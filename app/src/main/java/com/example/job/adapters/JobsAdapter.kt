package com.example.job.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.job.R
import com.example.job.databinding.ItemJobsBinding
import com.example.job.model.Vacancy

class JobsAdapter(
    private var dataList: List<Vacancy>,
    private val clickListener: OnJobClickListener
) : RecyclerView.Adapter<JobsAdapter.ViewHolder>() {

    private var showAll = false

    fun updateData(newDataList: List<Vacancy>) {
        dataList = newDataList
        notifyDataSetChanged()
    }

    fun showAll(show: Boolean) {
        showAll = show
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemJobsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (showAll) dataList.size else dataList.take(3).size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (showAll) holder.bind(dataList[position], clickListener)
        else if (!showAll && position <= 3) holder.bind(dataList[position], clickListener)
    }

    class ViewHolder(binding: ItemJobsBinding) : RecyclerView.ViewHolder(binding.root) {

        val binding = binding

        fun bind(data: Vacancy, clickListener: OnJobClickListener) = with(binding) {
            val count = data.lookingNumber
            val str = if (count % 10 in 2..4 && (count % 100 !in 12..14)) "$count человека"
            else "$count человек"

            lookingNumber.setText("Сейчас просматривает ${str}")
            heartButton.setImageResource(if (data.isFavorite) R.drawable.ic_filled_heart else R.drawable.ic_unfilled_heart)
            if (data.salary.short != null) {
                salary.visibility = View.VISIBLE
                salary.setText("${data.salary.full}")
            }else {
                salary.visibility = View.GONE
            }
            title.setText(data.title)
            address.setText(data.address.town)
            company.setText(data.company)
            experience.setText(data.experience.previewText)
            publishedDate.setText("Опубликовано ${data.publishedDate}")

            jobsCard.setOnClickListener {
                clickListener.onJobClick(data.id)
            }

            heartButton.setOnClickListener {
                clickListener.onHeartClick(data.id)
            }

        }
    }

    interface OnJobClickListener {
        fun onJobClick(itemId: String)
        fun onHeartClick(itemId: String)
    }
}