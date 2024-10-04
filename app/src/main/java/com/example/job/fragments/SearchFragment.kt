package com.example.job.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.job.R
import com.example.job.adapters.JobsAdapter
import com.example.job.adapters.RecommendationsAdapter
import com.example.job.databinding.FragmentSearchBinding
import com.example.job.viewModel.SearchViewModel
import com.example.job.viewModel.SharedViewModel

class SearchFragment : Fragment(R.layout.fragment_search), JobsAdapter.OnJobClickListener {

    private lateinit var viewModel: SharedViewModel
    private lateinit var searchViewModel: SearchViewModel

    private val recommendationsAdapter = RecommendationsAdapter(arrayListOf())
    private val jobsAdapter = JobsAdapter(arrayListOf(), this)

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var showMoreJobsButton = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSearchBinding.bind(view)

        init()
    }

    fun init() = with(binding) {

        recommendations.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recommendations.adapter = recommendationsAdapter

        jobs.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        jobs.adapter = jobsAdapter

        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        searchViewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)

        val savedPosition = searchViewModel.getScrollPosition()
        searchNestedScrollView.scrollTo(0, savedPosition)

        viewModel.data.observe(viewLifecycleOwner) { data->
            if (data != null) {
                recommendationsAdapter.updateData(data.offers)
                jobsAdapter.updateData(data.vacancies)

                val vacanciesCount = data.vacancies.size
                jobsNumber.setText(
                    when {
                        vacanciesCount % 10 == 1 && vacanciesCount % 100 != 11 -> "$vacanciesCount вакансия"
                        vacanciesCount % 10 in 2..4 && (vacanciesCount % 100 !in 12..14) -> "$vacanciesCount вакансии"
                        else -> "$vacanciesCount вакансий"
                    }
                )
                val jobsLeft = vacanciesCount - 3
                if (jobsLeft > 0) {
                    moreJobsButton.setText(
                        when {
                            jobsLeft % 10 == 1 && jobsLeft % 100 != 11 -> "Ещё $jobsLeft вакансия"
                            jobsLeft % 10 in 2..4 && (jobsLeft % 100 !in 12..14) -> "Ещё $jobsLeft вакансии"
                            else -> "Ещё $jobsLeft вакансий"
                        }
                    )
                    showMoreJobsButton = true
                } else showMoreJobsButton = false

                showAllCards(searchViewModel.isAllCardsShown())
            }
        }

        moreJobsButton.setOnClickListener {
            showAllCards(true)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (searchViewModel.isAllCardsShown()) {
                        showAllCards(false)
                    }
                    else requireActivity().finishAffinity()
                }
            }
        )
    }

    override fun onPause() = with(binding) {
        super.onPause()
        val scrollPosition = searchNestedScrollView.scrollY
        searchViewModel.saveScrollPosition(scrollPosition)
    }

    fun showAllCards(flag: Boolean) = with(binding) {
        if (flag) {
            searchViewModel.setAllCardsShown(true)
            jobsAdapter.showAll(true)
            moreJobsButton.visibility = View.GONE
            jobsForYou.visibility = View.GONE
            recommendations.visibility = View.GONE
            jobsLinearLayout.visibility = View.VISIBLE
        } else {
            searchViewModel.setAllCardsShown(false)
            jobsAdapter.showAll(false)
            if (showMoreJobsButton)
            moreJobsButton.visibility = View.VISIBLE
            jobsForYou.visibility = View.VISIBLE
            recommendations.visibility = View.VISIBLE
            jobsLinearLayout.visibility = View.GONE
        }
    }

    override fun onJobClick(itemId: String) {
        val action = SearchFragmentDirections.actionSearchToJob(itemId, "searchFragment")
        findNavController().navigate(action)
    }

    override fun onHeartClick(itemId: String) {
        val data = viewModel.data.value
        data?.let {data->
            val flag = data.vacancies.find { it.id == itemId }!!.isFavorite.not()
            data.vacancies.find { it.id == itemId }!!.isFavorite = flag
            viewModel.updateData(data)
        }
    }
}