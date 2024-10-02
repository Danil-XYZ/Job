package com.example.job.fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.job.R
import com.example.job.databinding.FragmentPasswordBinding
import com.example.job.databinding.FragmentWelcomeBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView

class PasswordFragment : Fragment(R.layout.fragment_password) {

    private var _binding: FragmentPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentPasswordBinding.bind(view)

        init()
    }

    fun init() = with(binding) {

        val email = arguments?.let {
            PasswordFragmentArgs.fromBundle(it).email
        }

        title.setText(email)

        val items: List<EditText> = listOf(item1, item2, item3, item4)
        val stars: List<TextView> = listOf(text1, text2, text3, text4)

        val confirmButton: AppCompatButton = confirmButton

        val len = items.size

        for (i in 0 until len) {

            items[i].doOnTextChanged { text, _, _, _ ->
                if (text?.length == 1 && i != len - 1) {
                    items[i + 1].requestFocus()
                }
                if (text?.length == 0 && i != 0) {
                    items[i - 1].requestFocus()
                }
                activateButton(items, confirmButton)
            }
            items[i].setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) stars[i].visibility = View.INVISIBLE
                else if (items[i].text.length == 0) stars[i].visibility = View.VISIBLE
                else stars[i].visibility = View.INVISIBLE
            }
        }

        confirmButton.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
    }

    fun activateButton(items: List<EditText>, btn: AppCompatButton) {

        items.forEach {
            if (it.text.length == 0) {
                btn.isEnabled = false
                return
            }
        }
        btn.isEnabled = true
    }
}