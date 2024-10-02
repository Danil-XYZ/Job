package com.example.job.viewModel

import androidx.lifecycle.ViewModel

class FavoriteViewModel : ViewModel() {
    private var scrollPosition: Int = 0

    fun saveScrollPosition(position: Int) {
        scrollPosition = position
    }

    fun getScrollPosition(): Int {
        return scrollPosition
    }
}