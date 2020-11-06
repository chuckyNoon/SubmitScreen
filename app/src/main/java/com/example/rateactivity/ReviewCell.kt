package com.example.rateactivity

sealed class ReviewCell {

    abstract fun isSame(other: ReviewCell): Boolean
    abstract fun areContentsSame(other: ReviewCell): Boolean

    data class SurveyWithStarIcons(
        val question: String,
        val rating: Int,
        val isContentClickable: Boolean,
        val id: CellStatesSource.CellId
    ) : ReviewCell() {

        override fun isSame(other: ReviewCell): Boolean {
            return when (other) {
                !is SurveyWithStarIcons -> false
                else -> this.id == other.id
            }
        }

        override fun areContentsSame(other: ReviewCell): Boolean {
            return this == other
        }
    }

    data class SurveyWithPersonIcons(
        val question: String,
        val rating: Int,
        val isContentClickable: Boolean,
        val id: CellStatesSource.CellId
    ) : ReviewCell() {

        override fun isSame(other: ReviewCell): Boolean {
            return when (other) {
                !is SurveyWithPersonIcons -> false
                else -> this.id == other.id
            }
        }

        override fun areContentsSame(other: ReviewCell): Boolean {
            return this == other
        }
    }

    data class SurveyWithAltOption(
        val question: String,
        val rating: Int,
        val altOptionText: String,
        val isAltOptionSelected: Boolean,
        val isContentClickable: Boolean,
        val id: CellStatesSource.CellId
    ) : ReviewCell() {

        override fun isSame(other: ReviewCell): Boolean {
            return when (other) {
                !is SurveyWithAltOption -> false
                else -> this.id == other.id
            }
        }

        override fun areContentsSame(other: ReviewCell): Boolean {
            return this == other
        }
    }

    data class Feedback(
        val titleText: String,
        val feedbackText: String,
        val isContentClickable: Boolean,
        val id: CellStatesSource.CellId
    ) : ReviewCell() {

        override fun isSame(other: ReviewCell): Boolean {
            return when (other) {
                !is Feedback -> false
                else -> this.id == other.id
            }
        }

        override fun areContentsSame(other: ReviewCell): Boolean {
            return this == other
        }
    }

    data class Submit(
        val buttonText: String,
        val isContentClickable: Boolean,
        val id: CellStatesSource.CellId
    ) : ReviewCell() {

        override fun isSame(other: ReviewCell): Boolean {
            return when (other) {
                !is Submit -> false
                else -> this.id == other.id
            }
        }

        override fun areContentsSame(other: ReviewCell): Boolean {
            return this == other
        }
    }
}