package com.example.job.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.job.R
import com.example.job.adapters.JobsAdapter
import com.example.job.databinding.FragmentFavoritesBinding
import com.example.job.viewModel.FavoriteViewModel
import com.example.job.viewModel.SearchViewModel
import com.example.job.viewModel.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavoritesFragment : Fragment(R.layout.fragment_favorites), JobsAdapter.OnJobClickListener {

    private lateinit var viewModel: SharedViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel

    private val jobsAdapter = JobsAdapter(arrayListOf(), this)

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentFavoritesBinding.bind(view)

        init()
    }

    fun init() = with(binding) {
        favorites.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        favorites.adapter = jobsAdapter
        jobsAdapter.showAll(true)

        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        favoriteViewModel = ViewModelProvider(requireActivity()).get(FavoriteViewModel::class.java)

        val savedPosition = favoriteViewModel.getScrollPosition()
        favoritesNestedScrollView.scrollTo(0, savedPosition)

        viewModel.data.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                val tempList = data.vacancies.filter { it.isFavorite }
                jobsAdapter.updateData(tempList)

                val vacanciesCount = tempList.size
                jobsNumber.setText(
                    when {
                        vacanciesCount % 10 == 1 && vacanciesCount % 100 != 11 -> "$vacanciesCount вакансия"
                        vacanciesCount % 10 in 2..4 && (vacanciesCount % 100 !in 12..14) -> "$vacanciesCount вакансии"
                        else -> "$vacanciesCount вакансий"
                    }
                )
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finishAffinity()
                }
            }
        )
    }

    override fun onPause() = with(binding) {
        super.onPause()
        val scrollPosition = favoritesNestedScrollView.scrollY
        favoriteViewModel.saveScrollPosition(scrollPosition)
    }

    override fun onJobClick(itemId: String) {
        val action = FavoritesFragmentDirections.actionFavoritesToJob(itemId, "favoriteFragment")
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