package com.example.rateactivity

object ReviewModelBuilder {

    private const val DEFAULT_AIRCRAFT_RATING = 1
    private const val DEFAULT_CREW_RATING = 1
    private const val DEFAULT_SEAT_RATING = 1
    private const val DEFAULT_PEOPLE_RATING = 1
    private const val DEFAULT_FOOD_RATING = 1
    private const val DEFAULT_FEEDBACK_TEXT = ""

    fun build(headerState: HeaderState, cellStates: ArrayList<CellState>): ReviewModel {
        var aircraft = DEFAULT_AIRCRAFT_RATING
        var crew = DEFAULT_CREW_RATING
        var seat = DEFAULT_SEAT_RATING
        var people = DEFAULT_PEOPLE_RATING
        var food: Int? = DEFAULT_FOOD_RATING
        var text = DEFAULT_FEEDBACK_TEXT
        val flight = headerState.rating + 1

        for (state in cellStates) {
            if (aircraft == DEFAULT_AIRCRAFT_RATING) aircraft = tryGetAircraftRating(state)
            if (crew == DEFAULT_CREW_RATING) crew = tryGetCrewRating(state)
            if (seat == DEFAULT_SEAT_RATING) seat = tryGetSeatRating(state)
            if (people == DEFAULT_PEOPLE_RATING) people = tryGetPeopleRating(state)
            if (food == DEFAULT_FOOD_RATING) food = tryGetFoodRating(state)
            if (text == DEFAULT_FEEDBACK_TEXT) text = tryGetFeedbackText(state)
        }
        return ReviewModel(text, food, flight, crew, aircraft, seat, people)
    }

    private fun tryGetAircraftRating(state: CellState): Int {
        if (state is CellState.SurveyWithStarIcons &&
            state.id == CellStatesSource.CellId.Aircraft
        ) {
            return state.rating + 1
        }
        return DEFAULT_AIRCRAFT_RATING
    }

    private fun tryGetCrewRating(state: CellState): Int {
        if (state is CellState.SurveyWithStarIcons &&
            state.id == CellStatesSource.CellId.Crew
        ) {
            return state.rating + 1
        }
        return DEFAULT_CREW_RATING
    }

    private fun tryGetSeatRating(state: CellState): Int {
        if (state is CellState.SurveyWithStarIcons &&
            state.id == CellStatesSource.CellId.Seat
        ) {
            return state.rating + 1
        }
        return DEFAULT_SEAT_RATING
    }

    private fun tryGetPeopleRating(state: CellState): Int {
        if (state is CellState.SurveyWithPersonIcons &&
            state.id == CellStatesSource.CellId.People
        ) {
            return state.rating + 1
        }
        return DEFAULT_PEOPLE_RATING
    }

    private fun tryGetFoodRating(state: CellState): Int? {
        if (state !is CellState.SurveyWithOption ||
            state.id != CellStatesSource.CellId.Food
        ) {
            return DEFAULT_FOOD_RATING
        }
        return if (state.isAltOptionSelected) {
            0
        } else
            (state.rating + 1)
    }


    private fun tryGetFeedbackText(state: CellState): String {
        if (state is CellState.Feedback &&
            state.id == CellStatesSource.CellId.Feedback
        ) {
            return state.feedbackText
        }
        return DEFAULT_FEEDBACK_TEXT
    }

}

data class ReviewModel(
    val text: String,
    val food: Int?,
    val flight: Int,
    val crew: Int,
    val aircraft: Int,
    val seat: Int,
    val people: Int
)