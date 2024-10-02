package com.example.job.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.job.R
import com.example.job.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentWelcomeBinding.bind(view)

        init()
    }

    @SuppressLint("ResourceType")
    fun init() = with(binding) {

        continueButton.setOnClickListener {
            val isValid =
                android.util.Patterns.EMAIL_ADDRESS.matcher(welcomeEditText.text).matches()
            if (isValid) {
                val action =
                    WelcomeFragmentDirections.actionWelcomeToPassword(welcomeEditText.text.toString())
                findNavController().navigate(action)
            } else {
                welcomeEditText.setBackgroundResource(R.drawable.rounded_edittext_with_border)
                mistake.visibility = View.VISIBLE
            }
        }

        welcomeEditText.addTextChangedListener {
            if (welcomeEditText.text.toString() != "") {
                continueButton.isEnabled = true
                crossButton.visibility = View.VISIBLE
            } else {
                continueButton.isEnabled = false
                crossButton.visibility = View.GONE
            }
            welcomeEditText.setBackgroundResource(R.drawable.rounded_edittext)
            mistake.visibility = View.GONE
        }

        crossButton.setOnClickListener {
            welcomeEditText.setText("")
            it.visibility = View.GONE
        }

        welcomeEditText.setOnFocusChangeListener { _, hasFocus ->
            welcomeEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            welcomeEditText.hint = ""
        }
    }
}