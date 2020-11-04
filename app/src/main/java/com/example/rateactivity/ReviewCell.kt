package com.example.rateactivity

sealed class ReviewCell {

    abstract fun isSame(other: ReviewCell): Boolean
    abstract fun areContentsSame(other: ReviewCell): Boolean

    data class SurveyWithStarIcons(
        val question: String,
        val rating: Int,
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

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as SurveyWithStarIcons

            if (question != other.question) return false
            if (rating != other.rating) return false
            if (id != other.id) return false

            return true
        }

        override fun hashCode(): Int {
            var result = question.hashCode()
            result = 31 * result + rating
            result = 31 * result + id.hashCode()
            return result
        }
    }

    data class SurveyWithPersonIcons(
        val question: String,
        val rating: Int,
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

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as SurveyWithPersonIcons

            if (question != other.question) return false
            if (rating != other.rating) return false
            if (id != other.id) return false

            return true
        }

        override fun hashCode(): Int {
            var result = question.hashCode()
            result = 31 * result + rating
            result = 31 * result + id.hashCode()
            return result
        }
    }

    data class SurveyWithAltOption(
        val question: String,
        val rating: Int,
        val altOptionText: String,
        val altOptionSelected: Boolean,
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

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as SurveyWithAltOption

            if (question != other.question) return false
            if (rating != other.rating) return false
            if (altOptionText != other.altOptionText) return false
            if (altOptionSelected != other.altOptionSelected) return false
            if (id != other.id) return false

            return true
        }

        override fun hashCode(): Int {
            var result = question.hashCode()
            result = 31 * result + rating
            result = 31 * result + altOptionText.hashCode()
            result = 31 * result + altOptionSelected.hashCode()
            result = 31 * result + id.hashCode()
            return result
        }
    }

    data class Feedback(
        val titleText: String,
        val feedbackText: String,
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

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Feedback

            if (titleText != other.titleText) return false
            if (feedbackText != other.feedbackText) return false
            if (id != other.id) return false

            return true
        }

        override fun hashCode(): Int {
            var result = titleText.hashCode()
            result = 31 * result + feedbackText.hashCode()
            result = 31 * result + id.hashCode()
            return result
        }
    }

    data class Submit(
        val buttonText: String,
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

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Submit

            if (buttonText != other.buttonText) return false
            if (id != other.id) return false

            return true
        }

        override fun hashCode(): Int {
            var result = buttonText.hashCode()
            result = 31 * result + id.hashCode()
            return result
        }
    }
}