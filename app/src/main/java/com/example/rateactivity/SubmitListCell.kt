package com.example.rateactivity

sealed class SubmitListCell {

    abstract fun isSame(other: SubmitListCell): Boolean
    abstract fun areContentsSame(other: SubmitListCell): Boolean

    data class SurveyWithStarIcons(
        val question: String,
        val estimation: Int,
        val id: SubmitScreenData.ItemId
    ) : SubmitListCell() {

        override fun isSame(other: SubmitListCell): Boolean {
            return when (other) {
                !is SurveyWithStarIcons -> false
                else -> this.id == other.id
            }
        }

        override fun areContentsSame(other: SubmitListCell): Boolean {
            return when {
                other !is SurveyWithStarIcons -> false
                question != other.question -> false
                estimation != other.estimation -> false
                else -> true
            }
        }
    }

    data class SurveyWithPersonIcons(
        val question: String,
        val estimation: Int,
        val id: SubmitScreenData.ItemId
    ) : SubmitListCell() {

        override fun isSame(other: SubmitListCell): Boolean {
            return when (other) {
                !is SurveyWithPersonIcons -> false
                else -> this.id == other.id
            }
        }

        override fun areContentsSame(other: SubmitListCell): Boolean {
            return when {
                other !is SurveyWithPersonIcons -> false
                question != other.question -> false
                estimation != other.estimation -> false
                else -> true
            }
        }
    }

    data class SurveyWithOption(
        val question: String,
        val estimation: Int,
        val altOptionText: String,
        val altOptionSelected: Boolean,
        val id: SubmitScreenData.ItemId
    ) : SubmitListCell() {

        override fun isSame(other: SubmitListCell): Boolean {
            return when (other) {
                !is SurveyWithOption -> false
                else -> this.id == other.id
            }
        }

        override fun areContentsSame(other: SubmitListCell): Boolean {
            return when {
                other !is SurveyWithOption -> false
                this.altOptionSelected != other.altOptionSelected -> false
                this.altOptionText != other.altOptionText -> false
                this.question != other.question -> false
                else -> this.estimation == other.estimation
            }
        }
    }

    data class Feedback(
        val titleText: String,
        val feedbackText: String,
        val id: SubmitScreenData.ItemId
    ) : SubmitListCell() {

        override fun isSame(other: SubmitListCell): Boolean {
            return when (other) {
                !is Feedback -> false
                else -> this.id == other.id
            }
        }

        override fun areContentsSame(other: SubmitListCell): Boolean {
            return when {
                other !is Feedback -> false
                this.feedbackText != other.feedbackText -> false
                else -> this.titleText == other.titleText
            }
        }
    }

    data class Submit(
        val buttonText: String,
        val id: SubmitScreenData.ItemId
    ) : SubmitListCell() {
        override fun isSame(other: SubmitListCell): Boolean {
            return when (other) {
                !is Submit -> false
                else -> this.id == other.id
            }
        }

        override fun areContentsSame(other: SubmitListCell): Boolean {
            return when (other) {
                !is Submit -> false
                else -> this.buttonText == other.buttonText
            }
        }
    }
}