package com.example.rateactivity

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_survey_with_person_icons.view.*
import kotlinx.android.synthetic.main.item_survey_with_alternative_option.view.*
import kotlinx.android.synthetic.main.item_feedback.view.*
import kotlinx.android.synthetic.main.item_submit.view.*

class ItemsAdapter(
    private val layoutInflater: LayoutInflater,
    private val surveyWithStarIconsInteraction: SurveyWithStarIconsInteraction,
    private val surveyWithPersonIconsInteraction: SurveyWithPersonIconsInteraction,
    private val surveyWithOptionInteraction: SurveyWithOptionInteraction,
    private val feedbackInteraction: FeedbackInteraction,
    private val submitInteraction: SubmitInteraction
) : RecyclerView.Adapter<ItemsAdapter.RowHolder>() {

    private enum class ViewType {
        SurveyWithPersonIcons,
        SurveyWithStarIcons,
        SurveyWithOption,
        Feedback,
        Submit
    }

    private val SubmitListCell.viewType: ViewType
        get() = when (this) {
            is SubmitListCell.SurveyWithPersonIcons -> ViewType.SurveyWithPersonIcons
            is SubmitListCell.SurveyWithStarIcons -> ViewType.SurveyWithStarIcons
            is SubmitListCell.SurveyWithOption -> ViewType.SurveyWithOption
            is SubmitListCell.Feedback -> ViewType.Feedback
            is SubmitListCell.Submit -> ViewType.Submit
        }

    private val viewTypeValues = ViewType.values()

    private val diffUtilCallback = object : DiffUtil.ItemCallback<SubmitListCell>() {
        override fun areItemsTheSame(oldItem: SubmitListCell, newItem: SubmitListCell) =
            oldItem.isSame(newItem)

        override fun areContentsTheSame(oldItem: SubmitListCell, newItem: SubmitListCell) =
            oldItem.areContentsSame(newItem)

        override fun getChangePayload(oldItem: SubmitListCell, newItem: SubmitListCell): Any? {
            return newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffUtilCallback)

    fun submitCells(cells: List<SubmitListCell>) {
        differ.submitList(cells)
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)

        if (payloads.size < 1)
            return
        when (val newData = payloads[0]) {
            is SubmitListCell.SurveyWithStarIcons -> holder.bind(newData)
            is SubmitListCell.SurveyWithOption -> holder.bind(newData)
            is SubmitListCell.Feedback -> holder.bind(newData)
            is SubmitListCell.Submit -> holder.bind(newData)
        }
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewTypeOrdinal: Int): RowHolder =
        when (viewTypeValues[viewTypeOrdinal]) {
            ViewType.SurveyWithStarIcons -> SurveyWithStarIconsHolder(
                layoutInflater,
                parent,
                surveyWithStarIconsInteraction
            )
            ViewType.SurveyWithPersonIcons -> SurveyWithPersonIconsHolder(
                layoutInflater,
                parent,
                surveyWithPersonIconsInteraction
            )
            ViewType.SurveyWithOption -> SurveyWithOptionHolder(
                layoutInflater,
                parent,
                surveyWithOptionInteraction
            )
            ViewType.Feedback -> FeedbackHolder(layoutInflater, parent, feedbackInteraction)
            ViewType.Submit -> SubmitHolder(layoutInflater, parent, submitInteraction)
        }

    override fun getItemViewType(position: Int): Int = differ.currentList[position].viewType.ordinal

    override fun getItemCount(): Int = differ.currentList.size

    abstract class RowHolder(v: View) : RecyclerView.ViewHolder(v) {
        abstract fun bind(cell: SubmitListCell)
    }

    class SurveyWithStarIconsHolder(private val v: View) :
        RowHolder(v) {

        private var questionTextView: TextView = v.questionTextView
        private var ratingBar: RatingBar = v.ratingBar
        private var interaction: SurveyWithStarIconsInteraction? = null

        constructor(
            layoutInflater: LayoutInflater,
            parent: ViewGroup,
            interaction: Interaction
        ) : this(layoutInflater.inflate(R.layout.item_survey_with_star_icons, parent, false)) {
            if (interaction is SurveyWithStarIconsInteraction) {
                this.interaction = interaction
            }
        }

        override fun bind(cell: SubmitListCell) {
            if (cell is SubmitListCell.SurveyWithStarIcons) {
                questionTextView.text = cell.question
                ratingBar.numStars = 5
                ratingBar.rating = cell.estimation.toFloat()
                ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                    interaction?.onRatingChanged(rating.toInt(), adapterPosition)
                }
            }
        }
    }

    class SurveyWithPersonIconsHolder(private val v: View) :
        RowHolder(v) {

        private var questionTextView: TextView = v.questionTextView
        private var ratingBar: RatingBar = v.ratingBar
        private var interaction: SurveyWithPersonIconsInteraction? = null

        constructor(
            layoutInflater: LayoutInflater,
            parent: ViewGroup,
            interaction: Interaction
        ) : this(layoutInflater.inflate(R.layout.item_survey_with_person_icons, parent, false)) {
            if (interaction is SurveyWithPersonIconsInteraction) {
                this.interaction = interaction
            }
        }

        override fun bind(cell: SubmitListCell) {
            if (cell is SubmitListCell.SurveyWithPersonIcons) {
                questionTextView.text = cell.question
                ratingBar.numStars = 5
                ratingBar.rating = cell.estimation.toFloat()
                ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                    interaction?.onRatingChanged(rating.toInt(), adapterPosition)
                }
            }
        }
    }

    class SurveyWithOptionHolder(private val v: View) :
        RowHolder(v) {

        private var interaction: SurveyWithOptionInteraction? = null
        private var questionTextView: TextView = v.questionTextView2
        private var ratingBar: RatingBar = v.ratingBar2
        private var radioButton: RadioButton = v.radioButton

        constructor(
            layoutInflater: LayoutInflater,
            parent: ViewGroup,
            interaction: Interaction
        ) : this(
            layoutInflater.inflate(
                R.layout.item_survey_with_alternative_option,
                parent,
                false
            )
        ) {
            if (interaction is SurveyWithOptionInteraction) {
                this.interaction = interaction
            }
        }

        override fun bind(cell: SubmitListCell) {
            if (cell is SubmitListCell.SurveyWithOption) {
                questionTextView.text = cell.question
                ratingBar.numStars = 5
                if (cell.altOptionSelected) {
                    ratingBar.rating = 0f
                    ratingBar.setIsIndicator(true)
                    radioButton.isChecked = true
                } else {
                    ratingBar.rating = cell.estimation.toFloat()
                    ratingBar.setIsIndicator(false)
                    radioButton.isChecked = false
                }
                radioButton.text = cell.altOptionText
                ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                    interaction?.onRatingChanged(rating.toInt(), adapterPosition)
                }
                radioButton.setOnClickListener {
                    interaction?.onAltOptionClicked(adapterPosition)
                }
            }
        }
    }

    class FeedbackHolder(private val v: View) :
        RowHolder(v) {

        private var interaction: FeedbackInteraction? = null
        private var feedbackTitleTextView: TextView = v.feedbackTitleTextView
        private var feedbackEditText: EditText = v.feedbackEditText

        constructor(
            layoutInflater: LayoutInflater,
            parent: ViewGroup,
            interaction: Interaction
        ) : this(layoutInflater.inflate(R.layout.item_feedback, parent, false)) {
            if (interaction is FeedbackInteraction) {
                this.interaction = interaction
            }
        }

        override fun bind(cell: SubmitListCell) {
            if (cell is SubmitListCell.Feedback) {
                feedbackTitleTextView.text = cell.titleText
                feedbackEditText.setText(cell.feedbackText)
                feedbackEditText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable) {
                        interaction?.onTextChanged(s.toString(), adapterPosition)
                    }

                    override fun beforeTextChanged(
                        s: CharSequence, start: Int,
                        count: Int, after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence, start: Int,
                        before: Int, count: Int
                    ) {
                    }
                })
            }
        }
    }

    class SubmitHolder(private val v: View) :
        RowHolder(v) {

        private var interaction: SubmitInteraction? = null
        private val submitButton: Button = v.submitButton

        constructor(
            layoutInflater: LayoutInflater,
            parent: ViewGroup,
            listener: Interaction
        ) : this(layoutInflater.inflate(R.layout.item_submit, parent, false)) {
            if (listener is SubmitInteraction) {
                interaction = listener
            }
        }

        override fun bind(cell: SubmitListCell) {
            if (cell is SubmitListCell.Submit) {
                submitButton.text = cell.buttonText
                submitButton.setOnClickListener {
                    interaction?.onSubmitButtonClicked()
                }
            }
        }
    }

    interface Interaction

    interface SurveyWithStarIconsInteraction : Interaction {
        fun onRatingChanged(rating: Int, position: Int)
    }

    interface SurveyWithPersonIconsInteraction : Interaction {
        fun onRatingChanged(rating: Int, position: Int)
    }

    interface SurveyWithOptionInteraction : Interaction {
        fun onRatingChanged(rating: Int, position: Int)
        fun onAltOptionClicked(position: Int)
    }

    interface FeedbackInteraction : Interaction {
        fun onTextChanged(text: String, position: Int)
    }

    interface SubmitInteraction : Interaction {
        fun onSubmitButtonClicked()
    }
}