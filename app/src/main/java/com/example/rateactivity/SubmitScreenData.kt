package com.example.rateactivity

object SubmitScreenData {

    enum class ItemId {
        Feedback,
        Food,
        Flight,
        Crew,
        Aircraft,
        Seat,
        Submit
    }

    fun getStates(): ArrayList<ItemState> {
        val states = ArrayList<ItemState>()
        states.add(ItemState.SurveyWithPersonIcons("How crowded was the flight?", 0, ItemId.Flight))
        states.add(
            ItemState.SurveyWithStarIcons(
                "How do you rate the aircraft?",
                0,
                ItemId.Aircraft
            )
        )
        states.add(ItemState.SurveyWithStarIcons("How do you rate the seats?", 0, ItemId.Seat))
        states.add(ItemState.SurveyWithStarIcons("How do you rate the crew?", 0, ItemId.Crew))
        states.add(
            ItemState.SurveyWithOption(
                "How do you rate the food?",
                0,
                "There were no food",
                false,
                ItemId.Food
            )
        )
        states.add(ItemState.Feedback("Leave any feedback", "", ItemId.Feedback))
        states.add(ItemState.Submit("Submit", ItemId.Submit))
        return states
    }
}

sealed class ItemState {

    data class SurveyWithStarIcons(
        var questionText: String,
        var rating: Int,
        var id: SubmitScreenData.ItemId
    ) : ItemState()

    class SurveyWithPersonIcons(
        var questionText: String,
        var rating: Int,
        var id: SubmitScreenData.ItemId
    ) : ItemState()

    data class SurveyWithOption(
        var questionText: String,
        var rating: Int,
        var altOptionText: String,
        var altOptionSelected: Boolean,
        var id: SubmitScreenData.ItemId
    ) : ItemState()

    data class Feedback(
        var titleText: String,
        var feedbackText: String,
        var id: SubmitScreenData.ItemId
    ) : ItemState()

    data class Submit(
        var buttonText: String,
        var id: SubmitScreenData.ItemId
    ) : ItemState()
}

fun ItemState.toCell(): SubmitListCell {
    return when (this) {
        is ItemState.SurveyWithStarIcons -> SubmitListCell.SurveyWithStarIcons(
            questionText,
            rating,
            id
        )
        is ItemState.SurveyWithPersonIcons -> SubmitListCell.SurveyWithPersonIcons(
            questionText,
            rating,
            id
        )
        is ItemState.SurveyWithOption -> SubmitListCell.SurveyWithOption(
            questionText,
            rating,
            altOptionText,
            altOptionSelected,
            id
        )
        is ItemState.Feedback -> SubmitListCell.Feedback(
            titleText,
            feedbackText,
            id
        )
        is ItemState.Submit -> SubmitListCell.Submit(
            buttonText,
            id
        )
    }
}