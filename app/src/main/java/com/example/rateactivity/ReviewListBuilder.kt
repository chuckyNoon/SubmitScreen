package com.example.rateactivity

import android.content.res.Resources

object ReviewListBuilder {

    enum class CellId {
        Feedback,
        Food,
        Crew,
        Aircraft,
        Seat,
        Submit,
        People
    }

    fun build(res: Resources): ArrayList<CellState> {
        val states = ArrayList<CellState>()
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