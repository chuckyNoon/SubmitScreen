package com.example.rateactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SubmitViewModel : ViewModel() {
    private var states = ArrayList<ItemState>()

    private val _cells = MutableLiveData<ArrayList<SubmitListCell>>()
    val cells: LiveData<ArrayList<SubmitListCell>> = _cells

    init {
        states = SubmitScreenData.getStates()
        fitCellsToStates()
    }

    private fun fitCellsToStates() {
        val newCells = ArrayList<SubmitListCell>()
        for (state in states) {
            newCells.add(state.toCell())
        }
        _cells.value = newCells
    }

    fun onEstimateChanged(rating: Int, position: Int) {
        when (val state = states[position]) {
            is ItemState.SurveyWithStarIcons -> state.rating = rating
            is ItemState.SurveyWithPersonIcons -> state.rating = rating
            is ItemState.SurveyWithOption -> state.rating = rating
            else -> return
        }
        fitCellsToStates()
    }

    fun onAltOptionButtonClicked(position: Int) {
        val state = states[position]
        if (state is ItemState.SurveyWithOption) {
            state.altOptionSelected = !state.altOptionSelected
            fitCellsToStates()
        }
    }

    fun onFeedbackTextChanged(text: String, position: Int) {
        if (states[position] is ItemState.Feedback) {
            val state = states[position] as ItemState.Feedback
            state.feedbackText = text
            fitCellsToStates()
        }
    }
}

data class SubmitState(
    var text: String = "",
    var food: Int = 1,
    var flight: Int = 1,
    var crew: Int = 1,
    var aircraft: Int = 1,
    var seat: Int = 1,
    var people: Int = 1
)