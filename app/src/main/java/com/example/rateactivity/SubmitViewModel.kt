package com.example.rateactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SubmitViewModel : ViewModel() {
    private var cellStates = ArrayList<ItemState>()

    private val _cells = MutableLiveData<ArrayList<SubmitListCell>>()
    val cells: LiveData<ArrayList<SubmitListCell>> = _cells

    init {
        cellStates = SubmitScreenData.getStates()
        fitCellsToStates()
    }

    fun onEstimateChanged(rating: Int, position: Int) {
        when (val state = cellStates[position]) {
            is ItemState.SurveyWithStarIcons -> state.rating = rating
            is ItemState.SurveyWithPersonIcons -> state.rating = rating
            is ItemState.SurveyWithOption -> state.rating = rating
            else -> return
        }
        fitCellsToStates()
    }

    fun onAltOptionButtonClicked(position: Int) {
        val state = cellStates[position]
        if (state is ItemState.SurveyWithOption) {
            state.altOptionSelected = !state.altOptionSelected
            fitCellsToStates()
        }
    }

    fun onFeedbackTextChanged(text: String, position: Int) {
        if (cellStates[position] is ItemState.Feedback) {
            val state = cellStates[position] as ItemState.Feedback
            state.feedbackText = text
            fitCellsToStates()
        }
    }

    private fun fitCellsToStates() {
        val newCells = ArrayList<SubmitListCell>()
        for (state in cellStates) {
            newCells.add(state.toCell())
        }
        _cells.value = newCells
    }

    fun getSubmitState(): SubmitState {
        val submitState = SubmitState()
        for (cellState in cellStates) {
            when (cellState) {
                is ItemState.SurveyWithStarIcons -> fillSubmitWithDataFromSurveyWithStarIcons(
                    submitState,
                    cellState
                )
                is ItemState.SurveyWithPersonIcons -> fillSubmitWithDataFromSurveyWithPersonIcons(
                    submitState,
                    cellState
                )
                is ItemState.SurveyWithOption -> fillSubmitWithDataFromSurveyWithOption(
                    submitState,
                    cellState
                )
                is ItemState.Feedback -> fillSubmitWithDataFromFeedback(
                    submitState,
                    cellState
                )
            }
        }
        return submitState
    }

    private fun fillSubmitWithDataFromSurveyWithStarIcons(
        submitState: SubmitState,
        state: ItemState.SurveyWithStarIcons
    ) {
        when (state.id) {
            SubmitScreenData.ItemId.Aircraft -> submitState.aircraft = state.rating + 1
            SubmitScreenData.ItemId.Crew -> submitState.crew = state.rating + 1
            SubmitScreenData.ItemId.Seat -> submitState.seat = state.rating + 1
        }
    }

    private fun fillSubmitWithDataFromSurveyWithPersonIcons(
        submitState: SubmitState,
        state: ItemState.SurveyWithPersonIcons
    ) {
        when (state.id) {
            SubmitScreenData.ItemId.People -> submitState.people = state.rating + 1
        }
    }

    private fun fillSubmitWithDataFromSurveyWithOption(
        submitState: SubmitState,
        state: ItemState.SurveyWithOption
    ) {
        when (state.id) {
            SubmitScreenData.ItemId.Food -> {
                if (state.altOptionSelected) {
                    submitState.food = 0
                } else {
                    submitState.food = state.rating + 1
                }
            }
        }
    }

    private fun fillSubmitWithDataFromFeedback(
        submitState: SubmitState,
        state: ItemState.Feedback
    ) {
        when (state.id) {
            SubmitScreenData.ItemId.Feedback -> submitState.text = state.feedbackText
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