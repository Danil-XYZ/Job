package com.example.job.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.job.R
import com.example.job.databinding.FragmentJobBinding
import com.example.job.viewModel.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView

class JobFragment() : Fragment(R.layout.fragment_job) {

    private lateinit var viewModel: SharedViewModel
    private var _binding: FragmentJobBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentJobBinding.bind(view)

        init()
    }

    fun init() = with(binding) {

        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val fragment = arguments?.let {
        JobFragmentArgs.fromBundle(it).fragment
    }

        when(fragment) {
            "searchFragment" -> bottomNavigationView!!.menu.findItem(R.id.searchFragment).isChecked = true
            "favoriteFragment" -> bottomNavigationView!!.menu.findItem(R.id.favoritesFragment).isChecked = true
        }

        MapKitFactory.initialize(requireContext())

        val answers: List<AppCompatButton> = listOf(
            question1,
            question2,
            question3,
            question4
        )

        var bottomSheetFragment: CustomBottomSheetFragment
        answers.forEach { answer ->
            answer.setOnClickListener {
                bottomSheetFragment = CustomBottomSheetFragment.newInstance(answer.text.toString())
                bottomSheetFragment.show(
                    requireActivity().supportFragmentManager,
                    "CustomBottomSheet"
                )
            }
        }

        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        replyButton.setOnClickListener {
            bottomSheetFragment = CustomBottomSheetFragment()
            bottomSheetFragment.show(requireActivity().supportFragmentManager, "CustomBottomSheet")
        }

        val id = arguments?.let {
            JobFragmentArgs.fromBundle(it).id
        }

        val data = viewModel.data.value

        heartButton.setOnClickListener {
            data?.let {data->
                data.vacancies.find { it.id == id }!!.isFavorite = data.vacancies.find { it.id == id }!!.isFavorite.not()
                if (data.vacancies.find { it.id == id }!!.isFavorite) heartButton.setImageResource(R.drawable.ic_filled_heart)
                else heartButton.setImageResource(R.drawable.ic_unfilled_heart)
                viewModel.updateData(data)
            }
        }

        data?.let { data ->
            val vacancy = data.vacancies.find { it.id == id }
            vacancy?.let {
                if (vacancy.isFavorite) heartButton.setImageResource(R.drawable.ic_filled_heart)
                else heartButton.setImageResource(R.drawable.ic_unfilled_heart)
                title.setText(vacancy.title)
                salary.setText(vacancy.salary.full)
                experience.setText(vacancy.experience.text)
                schedules.setText(vacancy.schedules.joinToString(", "))
                if (vacancy.appliedNumber != 0) {
                    appliedNumber.setText(
                        when {
                            vacancy.appliedNumber % 10 == 1 && vacancy.appliedNumber % 100 != 11 -> "${vacancy.appliedNumber} человек уже откликнулся"
                            vacancy.appliedNumber % 10 in 2..4 && (vacancy.appliedNumber % 100 !in 12..14) -> "${vacancy.appliedNumber} человека уже откликнулись"
                            else -> "${vacancy.appliedNumber} человек уже откликнулось"
                        }
                    )
                    lookingNumber.setText(
                        when {
                            vacancy.lookingNumber % 10 == 1 && vacancy.lookingNumber % 100 != 11 -> "${vacancy.lookingNumber} человек сейчас смотрит"
                            vacancy.lookingNumber % 10 in 2..4 && (vacancy.lookingNumber % 100 !in 12..14) -> "${vacancy.lookingNumber} человека сейчас смотрят"
                            else -> "${vacancy.lookingNumber} человек сейчас смотрят"
                        }
                    )
                    watchingLayout.visibility = View.VISIBLE
                }
                company.setText(vacancy.company)
                address.setText("${vacancy.address.town}, ${vacancy.address.street}, ${vacancy.address.house}")
                description.setText(vacancy.description)
                responsibilities.setText(vacancy.responsibilities)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}