package com.example.rateactivity

sealed class ReviewCell {

    abstract fun isSame(other: ReviewCell): Boolean
    abstract fun areContentsSame(other: ReviewCell): Boolean

    data class SurveyWithStarIcons(
        val question: String,
        val rating: Int,
        val id: ReviewListBuilder.CellId
    ) : ReviewCell() {

        override fun isSame(other: ReviewCell): Boolean {
            return when (other) {
                !is SurveyWithStarIcons -> false
                else -> this.id == other.id
            }
        }

        override fun areContentsSame(other: ReviewCell): Boolean {
            return when {
                other !is SurveyWithStarIcons -> false
                question != other.question -> false
                rating != other.rating -> false
                else -> true
            }
        }
    }

    data class SurveyWithPersonIcons(
        val question: String,
        val rating: Int,
        val id: ReviewListBuilder.CellId
    ) : ReviewCell() {

        override fun isSame(other: ReviewCell): Boolean {
            return when (other) {
                !is SurveyWithPersonIcons -> false
                else -> this.id == other.id
            }
        }

        override fun areContentsSame(other: ReviewCell): Boolean {
            return when {
                other !is SurveyWithPersonIcons -> false
                question != other.question -> false
                rating != other.rating -> false
                else -> true
            }
        }
    }

    data class SurveyWithAltOption(
        val question: String,
        val rating: Int,
        val altOptionText: String,
        val altOptionSelected: Boolean,
        val id: ReviewListBuilder.CellId
    ) : ReviewCell() {

        override fun isSame(other: ReviewCell): Boolean {
            return when (other) {
                !is SurveyWithAltOption -> false
                else -> this.id == other.id
            }
        }

        override fun areContentsSame(other: ReviewCell): Boolean {
            return when {
                other !is SurveyWithAltOption -> false
                this.altOptionSelected != other.altOptionSelected -> false
                this.altOptionText != other.altOptionText -> false
                this.question != other.question -> false
                else -> this.rating == other.rating
            }
        }
    }

    data class Feedback(
        val titleText: String,
        val feedbackText: String,
        val id: ReviewListBuilder.CellId
    ) : ReviewCell() {

        override fun isSame(other: ReviewCell): Boolean {
            return when (other) {
                !is Feedback -> false
                else -> this.id == other.id
            }
        }

        override fun areContentsSame(other: ReviewCell): Boolean {
            return when {
                other !is Feedback -> false
                this.feedbackText != other.feedbackText -> false
                else -> this.titleText == other.titleText
            }
        }
    }

    data class Submit(
        val buttonText: String,
        val id: ReviewListBuilder.CellId
    ) : ReviewCell() {
        override fun isSame(other: ReviewCell): Boolean {
            return when (other) {
                !is Submit -> false
                else -> this.id == other.id
            }
        }

        override fun areContentsSame(other: ReviewCell): Boolean {
            return when (other) {
                !is Submit -> false
                else -> this.buttonText == other.buttonText
            }
        }
    }
}