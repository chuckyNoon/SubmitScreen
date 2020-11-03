package com.example.rateactivity

object ReviewModelBuilder {

    fun build(cellStates: ArrayList<CellState>): ReviewModel {
        val review = ReviewModel()
        for (cellState in cellStates) {
            when (cellState) {
                is CellState.SurveyWithStarIcons -> fillReviewWithDataFromSurveyWithStarIcons(
                    review,
                    cellState
                )
                is CellState.SurveyWithPersonIcons -> fillReviewWithDataFromSurveyWithPersonIcons(
                    review,
                    cellState
                )
                is CellState.SurveyWithOption -> fillReviewWithDataFromSurveyWithOption(
                    review,
                    cellState
                )
                is CellState.Feedback -> fillReviewWithDataFromFeedback(
                    review,
                    cellState
                )
            }
        }
        return review
    }

    private fun fillReviewWithDataFromSurveyWithStarIcons(
        reviewModel: ReviewModel,
        state: CellState.SurveyWithStarIcons
    ) {
        when (state.id) {
            ReviewListBuilder.CellId.Aircraft -> reviewModel.aircraft = state.rating + 1
            ReviewListBuilder.CellId.Crew -> reviewModel.crew = state.rating + 1
            ReviewListBuilder.CellId.Seat -> reviewModel.seat = state.rating + 1
        }
    }

    private fun fillReviewWithDataFromSurveyWithPersonIcons(
        reviewModel: ReviewModel,
        state: CellState.SurveyWithPersonIcons
    ) {
        when (state.id) {
            ReviewListBuilder.CellId.People -> reviewModel.people = state.rating + 1
        }
    }

    private fun fillReviewWithDataFromSurveyWithOption(
        reviewModel: ReviewModel,
        state: CellState.SurveyWithOption
    ) {
        when (state.id) {
            ReviewListBuilder.CellId.Food -> {
                if (state.altOptionSelected) {
                    reviewModel.food = 0
                } else {
                    reviewModel.food = state.rating + 1
                }
            }
        }
    }

    private fun fillReviewWithDataFromFeedback(
        reviewModel: ReviewModel,
        state: CellState.Feedback
    ) {
        when (state.id) {
            ReviewListBuilder.CellId.Feedback -> reviewModel.text = state.feedbackText
        }
    }
}

data class ReviewModel(
    var text: String = "",
    var food: Int = 1,
    var flight: Int = 1,
    var crew: Int = 1,
    var aircraft: Int = 1,
    var seat: Int = 1,
    var people: Int = 1
)