package com.example.job.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.job.R
import com.example.job.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CustomBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentBottomSheetBinding.bind(view)

        init()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun init() = with(binding) {
        var isEditing = false

        val initialText = arguments?.getString("initial_text")

        initialText?.let {
            coverLetter.setText(it)
            addCoverLetter.visibility = View.GONE
            coverLetter.visibility = View.VISIBLE
            isEditing = true
        }

        coverLetter.setOnTouchListener { v, event ->
            if (v.id == R.id.coverLetter) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }

        replyButton.setOnClickListener {
            dismiss()
        }

        addCoverLetter.setOnClickListener {
            it.visibility = View.GONE
            coverLetter.visibility = View.VISIBLE
            isEditing = true
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isEditing) {
                        addCoverLetter.visibility = View.VISIBLE
                        coverLetter.visibility = View.GONE
                        isEditing = false
                    } else dismiss()
                }
            })
    }

    override fun onStart() {
        super.onStart()
        dialog?.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.isHideable = true
                behavior.skipCollapsed = true
            }
        }
    }

    companion object {
        fun newInstance(initialText: String): CustomBottomSheetFragment {
            val fragment = CustomBottomSheetFragment()
            val args = Bundle()
            args.putString("initial_text", initialText)
            fragment.arguments = args
            return fragment
        }
    }

}