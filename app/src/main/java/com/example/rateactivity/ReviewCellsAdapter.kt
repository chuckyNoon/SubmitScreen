package com.example.rateactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_survey_with_person_icons.view.*
import kotlinx.android.synthetic.main.item_survey_with_alternative_option.view.*
import kotlinx.android.synthetic.main.item_feedback.view.*
import kotlinx.android.synthetic.main.item_submit.view.*

class ReviewCellsAdapter(
    private val layoutInflater: LayoutInflater,
    private val surveyWithStarIconsInteraction: SurveyWithStarIconsInteraction,
    private val surveyWithPersonIconsInteraction: SurveyWithPersonIconsInteraction,
    private val surveyWithOptionInteraction: SurveyWithOptionInteraction,
    private val feedbackInteraction: FeedbackInteraction,
    private val submitInteraction: SubmitInteraction
) : RecyclerView.Adapter<ReviewCellsAdapter.ViewHolder>() {

    private enum class ViewType {
        SurveyWithPersonIcons,
        SurveyWithStarIcons,
        SurveyWithOption,
        Feedback,
        Submit
    }

    private val ReviewCell.viewType: ViewType
        get() = when (this) {
            is ReviewCell.SurveyWithPersonIcons -> ViewType.SurveyWithPersonIcons
            is ReviewCell.SurveyWithStarIcons -> ViewType.SurveyWithStarIcons
            is ReviewCell.SurveyWithAltOption -> ViewType.SurveyWithOption
            is ReviewCell.Feedback -> ViewType.Feedback
            is ReviewCell.Submit -> ViewType.Submit
        }

    private val viewTypeValues = ViewType.values()

    private val diffUtilCallback = object : DiffUtil.ItemCallback<ReviewCell>() {
        override fun areItemsTheSame(oldItem: ReviewCell, newItem: ReviewCell) =
            oldItem.isSame(newItem)

        override fun areContentsTheSame(oldItem: ReviewCell, newItem: ReviewCell) =
            oldItem.areContentsSame(newItem)

        override fun getChangePayload(oldItem: ReviewCell, newItem: ReviewCell): Any? {
            return newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffUtilCallback)

    fun submitCells(cells: List<ReviewCell>) {
        differ.submitList(cells)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)

        if (payloads.size < 1)
            return
        when (val newData = payloads[0]) {
            is ReviewCell.SurveyWithStarIcons -> holder.bind(newData)
            is ReviewCell.SurveyWithAltOption -> holder.bind(newData)
            is ReviewCell.Feedback -> holder.bind(newData)
            is ReviewCell.Submit -> holder.bind(newData)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewTypeOrdinal: Int): ViewHolder =
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
            ViewType.Feedback -> FeedbackHolder(
                layoutInflater,
                parent,
                feedbackInteraction
            )
            ViewType.Submit -> SubmitHolder(
                layoutInflater,
                parent,
                submitInteraction
            )
        }

    override fun getItemViewType(position: Int): Int = differ.currentList[position].viewType.ordinal

    override fun getItemCount(): Int = differ.currentList.size

    abstract class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        abstract fun bind(cell: ReviewCell)
    }

    class SurveyWithStarIconsHolder(v: View) :
        ViewHolder(v) {

        private var questionTextView: TextView = v.findViewById(R.id.questionTextView)
        private var ratingBar: RatingBar = v.findViewById(R.id.ratingBar)
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

        override fun bind(cell: ReviewCell) {
            if (cell is ReviewCell.SurveyWithStarIcons) {
                questionTextView.text = cell.question
                ratingBar.numStars = 5
                ratingBar.rating = cell.rating.toFloat()
                ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                    interaction?.onRatingChanged(rating.toInt(), adapterPosition)
                }
            }
        }
    }

    class SurveyWithPersonIconsHolder(private val v: View) :
        ViewHolder(v) {

        private var questionTextView: TextView = v.findViewById(R.id.questionTextView)
        private var ratingBar: RatingBar = v.findViewById(R.id.ratingBar)
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

        override fun bind(cell: ReviewCell) {
            if (cell is ReviewCell.SurveyWithPersonIcons) {
                questionTextView.text = cell.question
                ratingBar.numStars = 5
                ratingBar.rating = cell.rating.toFloat()
                ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                    interaction?.onRatingChanged(rating.toInt(), adapterPosition)
                }
            }
        }
    }

    class SurveyWithOptionHolder(v: View) :
        ViewHolder(v) {

        private var interaction: SurveyWithOptionInteraction? = null
        private var questionTextView: TextView = v.findViewById(R.id.questionTextView)
        private var ratingBar: RatingBar = v.findViewById(R.id.ratingBar)
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

        override fun bind(cell: ReviewCell) {
            if (cell is ReviewCell.SurveyWithAltOption) {
                questionTextView.text = cell.question
                ratingBar.numStars = 5
                if (cell.altOptionSelected) {
                    ratingBar.rating = 0f
                    ratingBar.setIsIndicator(true)
                    radioButton.isChecked = true
                } else {
                    ratingBar.rating = cell.rating.toFloat()
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

    class FeedbackHolder(v: View) :
        ViewHolder(v) {

        private var interaction: FeedbackInteraction? = null
        private var feedbackTitleTextView: TextView = v.findViewById(R.id.feedbackTitleTextView)
        private var feedbackEditText: EditText = v.findViewById(R.id.feedbackEditText)

        constructor(
            layoutInflater: LayoutInflater,
            parent: ViewGroup,
            interaction: Interaction
        ) : this(layoutInflater.inflate(R.layout.item_feedback, parent, false)) {
            if (interaction is FeedbackInteraction) {
                this.interaction = interaction
            }
        }

        override fun bind(cell: ReviewCell) {
            if (cell is ReviewCell.Feedback) {
                feedbackTitleTextView.text = cell.titleText
                feedbackEditText.imeOptions = EditorInfo.IME_ACTION_DONE
                if (feedbackEditText.text.toString() != cell.feedbackText) {
                    feedbackEditText.setText(cell.feedbackText)
                }
                feedbackEditText.addTextChangedListener { editable ->
                    editable?.let {
                        interaction?.onTextChanged(it.toString(), adapterPosition)
                    }
                }
            }
        }
    }

    class SubmitHolder(v: View) :
        ViewHolder(v) {

        private var interaction: SubmitInteraction? = null
        private val submitButton: Button = v.findViewById(R.id.submitButton)

        constructor(
            layoutInflater: LayoutInflater,
            parent: ViewGroup,
            listener: Interaction
        ) : this(layoutInflater.inflate(R.layout.item_submit, parent, false)) {
            if (listener is SubmitInteraction) {
                interaction = listener
            }
        }

        override fun bind(cell: ReviewCell) {
            if (cell is ReviewCell.Submit) {
                submitButton.text = cell.buttonText
                submitButton.setOnClickListener {
                    interaction?.onSubmitButtonClicked(it)
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
        fun onSubmitButtonClicked(v: View)
    }
}