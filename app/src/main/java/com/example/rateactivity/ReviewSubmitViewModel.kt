package com.example.rateactivity

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ReviewSubmitViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReviewSubmitViewModel(application) as T
    }
}

class ReviewSubmitViewModel() : ViewModel() {

    private var cellStates = ArrayList<CellState>()

    private val _cells = MutableLiveData<ArrayList<ReviewCell>>()
    val cells: LiveData<ArrayList<ReviewCell>> = _cells

    constructor(app: Application) : this() {
        cellStates = ReviewListBuilder.build(app.resources)
        createCellsFromStates()
    }

    fun getReport() = ReviewModelBuilder.build(cellStates).toReport()

    fun onRatingChanged(rating: Int, position: Int) {
        when (val state = cellStates[position]) {
            is CellState.SurveyWithStarIcons -> state.rating = rating
            is CellState.SurveyWithPersonIcons -> state.rating = rating
            is CellState.SurveyWithOption -> state.rating = rating
            else -> return
        }
        createCellsFromStates()
    }

    fun onAltOptionButtonClicked(position: Int) {
        val state = cellStates[position]
        if (state is CellState.SurveyWithOption) {
            state.altOptionSelected = !state.altOptionSelected
            createCellsFromStates()
        }
    }

    fun onFeedbackTextChanged(text: String, position: Int) {
        if (cellStates[position] is CellState.Feedback) {
            val state = cellStates[position] as CellState.Feedback
            state.feedbackText = text
            createCellsFromStates()
        }
    }

    private fun createCellsFromStates() {
        val newCells = ArrayList<ReviewCell>()
        for (state in cellStates) {
            newCells.add(state.toCell())
        }
        _cells.value = newCells
    }

    private fun ReviewModel.toReport() = StringBuilder().apply {
        append(makeReportLine("text", text))
        append(makeReportLine("food", food.toString()))
        append(makeReportLine("flight", flight.toString()))
        append(makeReportLine("crew", crew.toString()))
        append(makeReportLine("aircraft", aircraft.toString()))
        append(makeReportLine("seat", seat.toString()))
        append(makeReportLine("people", people.toString()))
    }.toString()

    private fun makeReportLine(fieldName: String, value: String): String {
        return ("$fieldName = $value\n")
    }
}


sealed class CellState {

    data class SurveyWithStarIcons(
        var questionText: String,
        var rating: Int,
        var id: ReviewListBuilder.CellId
    ) : CellState()

    class SurveyWithPersonIcons(
        var questionText: String,
        var rating: Int,
        var id: ReviewListBuilder.CellId
    ) : CellState()

    data class SurveyWithOption(
        var questionText: String,
        var rating: Int,
        var altOptionText: String,
        var altOptionSelected: Boolean,
        var id: ReviewListBuilder.CellId
    ) : CellState()

    data class Feedback(
        var titleText: String,
        var feedbackText: String,
        var id: ReviewListBuilder.CellId
    ) : CellState()

    data class Submit(
        var buttonText: String,
        var id: ReviewListBuilder.CellId
    ) : CellState()
}

fun CellState.toCell(): ReviewCell {
    return when (this) {
        is CellState.SurveyWithStarIcons -> ReviewCell.SurveyWithStarIcons(
            questionText,
            rating,
            id
        )
        is CellState.SurveyWithPersonIcons -> ReviewCell.SurveyWithPersonIcons(
            questionText,
            rating,
            id
        )
        is CellState.SurveyWithOption -> ReviewCell.SurveyWithAltOption(
            questionText,
            rating,
            altOptionText,
            altOptionSelected,
            id
        )
        is CellState.Feedback -> ReviewCell.Feedback(
            titleText,
            feedbackText,
            id
        )
        is CellState.Submit -> ReviewCell.Submit(
            buttonText,
            id
        )
    }
}