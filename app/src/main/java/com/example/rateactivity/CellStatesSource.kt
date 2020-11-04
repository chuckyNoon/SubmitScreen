package com.example.rateactivity

import android.app.Application

class CellStatesSource(private val app: Application) {

    enum class CellId {
        Feedback,
        Food,
        Crew,
        Aircraft,
        Seat,
        Submit,
        People
    }

    fun getStates(): ArrayList<CellState> {
        val states = ArrayList<CellState>()
        val res = app.resources
        states.add(
            CellState.SurveyWithPersonIcons(
                res.getString(R.string.people_question),
                0,
                CellId.People
            )
        )
        states.add(
            CellState.SurveyWithStarIcons(
                res.getString(R.string.aircraft_question),
                0,
                CellId.Aircraft
            )
        )
        states.add(
            CellState.SurveyWithStarIcons(
                res.getString(R.string.seats_question),
                0,
                CellId.Seat
            )
        )
        states.add(
            CellState.SurveyWithStarIcons(
                res.getString(R.string.crew_question),
                0,
                CellId.Crew
            )
        )
        states.add(
            CellState.SurveyWithOption(
                res.getString(R.string.food_question),
                0,
                res.getString(R.string.no_food_answer),
                false,
                CellId.Food
            )
        )
        states.add(
            CellState.Feedback(
                res.getString(R.string.feedback_title),
                "",
                CellId.Feedback
            )
        )
        states.add(
            CellState.Submit(
                res.getString(R.string.submit_button_text),
                CellId.Submit
            )
        )
        return states
    }
}

sealed class CellState {

    class SurveyWithStarIcons(
        var questionText: String,
        var rating: Int,
        var id: CellStatesSource.CellId
    ) : CellState()

    class SurveyWithPersonIcons(
        var questionText: String,
        var rating: Int,
        var id: CellStatesSource.CellId
    ) : CellState()

    class SurveyWithOption(
        var questionText: String,
        var rating: Int,
        var altOptionText: String,
        var altOptionSelected: Boolean,
        var id: CellStatesSource.CellId
    ) : CellState()

    class Feedback(
        var titleText: String,
        var feedbackText: String,
        var id: CellStatesSource.CellId
    ) : CellState()

    class Submit(
        var buttonText: String,
        var id: CellStatesSource.CellId
    ) : CellState()
}
