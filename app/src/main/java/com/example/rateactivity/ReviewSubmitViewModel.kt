package com.example.rateactivity

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ReviewSubmitViewModelFactory(
    private val headerStateSource: HeaderStateSource,
    private val cellStatesSource: CellStatesSource
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReviewSubmitViewModel(headerStateSource, cellStatesSource) as T
    }
}

class ReviewSubmitViewModel(
    private val headerStateSource: HeaderStateSource,
    private val cellStatesSource: CellStatesSource
) : ViewModel() {

    private var headerState: HeaderState = headerStateSource.getState()
    private var cellStates: ArrayList<CellState> = cellStatesSource.getStates()

    private val _headerViewState = MutableLiveData<HeaderViewState>()
    private val _cells = MutableLiveData<ArrayList<ReviewCell>>()
    private val _motionLayoutProgress = MutableLiveData(0f)
    private val _isProgressBarVisible = MutableLiveData(false)
    private val _toastMessage = MutableLiveData<String?>(null)

    val headerViewState: LiveData<HeaderViewState> = _headerViewState
    val cells: LiveData<ArrayList<ReviewCell>> = _cells
    val motionLayoutProgress: LiveData<Float> = _motionLayoutProgress
    val isProgressBarVisible: LiveData<Boolean> = _isProgressBarVisible
    val toast: LiveData<String?> = _toastMessage

    init {
        createCellsFromStates()
        createHeaderFromState()
    }

    fun onStop(motionLayoutProgress: Float?) {
        motionLayoutProgress?.let {
            _motionLayoutProgress.value = it
        }
    }

    fun onSubmitButtonClicked() {
        viewModelScope.launch {
            _isProgressBarVisible.value = true
            changeViewsClickableState(isClickable = false)
            withContext(Dispatchers.Default) {
                delay(9000)
            }
            changeViewsClickableState(isClickable = true)
            _isProgressBarVisible.value = false
            _toastMessage.value = ReviewModelBuilder.build(headerState, cellStates).toReport()
            _toastMessage.value = null
        }
    }

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
            state.isAltOptionSelected = !state.isAltOptionSelected
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

    private fun changeViewsClickableState(isClickable:Boolean) {
        for (cellState in cellStates) {
            when (cellState) {
                is CellState.SurveyWithStarIcons -> cellState.isContentClickable = isClickable
                is CellState.SurveyWithPersonIcons -> cellState.isContentClickable = isClickable
                is CellState.SurveyWithOption -> cellState.isContentClickable = isClickable
                is CellState.Feedback -> cellState.isContentClickable = isClickable
                is CellState.Submit -> cellState.isContentClickable = isClickable
            }
        }
        headerState.isClickable = isClickable
        createCellsFromStates()
        createHeaderFromState()
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
        HeaderViewState(title, secondLine, thirdLine, rating, isClickable)
}

data class HeaderViewState(
    val title: String,
    val secondLine: String,
    val thirdLine: String,
    val rating: Int,
    val isClickable: Boolean
)

fun CellState.toCell(): ReviewCell {
    return when (this) {
        is CellState.SurveyWithStarIcons -> ReviewCell.SurveyWithStarIcons(
            questionText,
            rating,
            isContentClickable,
            id
        )
        is CellState.SurveyWithPersonIcons -> ReviewCell.SurveyWithPersonIcons(
            questionText,
            rating,
            isContentClickable,
            id
        )
        is CellState.SurveyWithOption -> ReviewCell.SurveyWithAltOption(
            questionText,
            rating,
            altOptionText,
            isAltOptionSelected,
            isContentClickable,
            id
        )
        is CellState.Feedback -> ReviewCell.Feedback(
            titleText,
            feedbackText,
            isContentClickable,
            id
        )
        is CellState.Submit -> ReviewCell.Submit(
            buttonText,
            isContentClickable,
            id
        )
    }
}