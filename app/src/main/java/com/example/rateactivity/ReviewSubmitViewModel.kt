package com.example.rateactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ReviewSubmitViewModelFactory(
    private val headerStateSource: HeaderStateSource,
    private val cellStatesSource: CellStatesSource
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReviewSubmitViewModel(headerStateSource, cellStatesSource) as T
    }
}


class ReviewSubmitViewModel() : ViewModel() {

    private lateinit var headerStateSource: HeaderStateSource
    private lateinit var cellStatesSource: CellStatesSource

    private lateinit var headerState: HeaderState
    private lateinit var cellStates: ArrayList<CellState>

    private val _headerViewState = MutableLiveData<HeaderViewState>()
    private val _cells = MutableLiveData<ArrayList<ReviewCell>>()

    val headerViewState: LiveData<HeaderViewState> = _headerViewState
    val cells: LiveData<ArrayList<ReviewCell>> = _cells

    constructor(
        headerStateSource: HeaderStateSource,
        cellStatesSource: CellStatesSource
    ) : this() {
        this.headerStateSource = headerStateSource
        this.cellStatesSource = cellStatesSource

        cellStates = cellStatesSource.getStates()
        headerState = headerStateSource.getState()

        createCellsFromStates()
        createHeaderFromState()
    }

    fun getReport() = ReviewModelBuilder.build(headerState, cellStates).toReport()

    fun onHeaderRatingChanged(rating: Int) {
        headerState.rating = rating
        createHeaderFromState()
    }

    fun onCellRatingChanged(rating: Int, position: Int) {
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

    private fun createHeaderFromState() {
        _headerViewState.value = headerState.toViewState()
    }

    private fun ReviewModel.toReport() = StringBuilder().apply {
        append(makeReportLine("flight", flight.toString()))
        append(makeReportLine("people", people.toString()))
        append(makeReportLine("aircraft", aircraft.toString()))
        append(makeReportLine("seat", seat.toString()))
        append(makeReportLine("crew", crew.toString()))
        append(makeReportLine("food", food.toString()))
        append(makeReportLine("text", text))
    }.toString()

    private fun makeReportLine(fieldName: String, value: String): String {
        return ("$fieldName = $value\n")
    }

    private fun HeaderState.toViewState(): HeaderViewState =
        HeaderViewState(title, secondLine, thirdLine, rating)
}

data class HeaderViewState(
    val title: String,
    val secondLine: String,
    val thirdLine: String,
    val rating: Int
)

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