package com.example.job.viewModel

import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    private var scrollPosition: Int = 0

    private var isAllCardsShown: Boolean = false

    fun saveScrollPosition(position: Int) {
        scrollPosition = position
    }

    fun getScrollPosition(): Int {
        return scrollPosition
    }

    fun setAllCardsShown(isShown: Boolean) {
        isAllCardsShown = isShown
    }

    fun isAllCardsShown(): Boolean {
        return isAllCardsShown
    }

}