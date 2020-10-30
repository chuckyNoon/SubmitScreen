package com.example.rateactivity

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_estimation.view.*
import kotlinx.android.synthetic.main.item_estimation_with_box.view.*
import kotlinx.android.synthetic.main.item_feedback.view.*
import kotlinx.android.synthetic.main.item_submit.view.*


sealed class MyCell {
    data class Estimation(var questionText: String, var img: Bitmap?, var estimationNum: Int) :
        MyCell()

    data class EstimationWithBox(
        var questionText: String,
        var img: Bitmap?,
        var estimationNum: Int,
        var boxText: String,
    ) : MyCell()

    data class Feedback(var titleText: String) : MyCell()
    data class Submit(var buttonText: String) : MyCell()
}

class ItemsAdapter(
    private val layoutInflater: LayoutInflater,
    private val items: ArrayList<MyCell>,
    private val estimateActionListener: EstimateActionListener
) : RecyclerView.Adapter<ItemsAdapter.RowHolder>() {

    private enum class ViewType {
        Estimation,
        EstimationWithBox,
        Feedback,
        Submit
    }

    private val MyCell.viewType: ViewType
        get() = when (this) {
            is MyCell.Estimation -> ViewType.Estimation
            is MyCell.EstimationWithBox -> ViewType.EstimationWithBox
            is MyCell.Feedback -> ViewType.Feedback
            is MyCell.Submit -> ViewType.Submit
        }

    private val viewTypeValues = ViewType.values()

    override fun onCreateViewHolder(parent: ViewGroup, viewTypeOrdinal: Int): RowHolder =
        when (viewTypeValues[viewTypeOrdinal]) {
            ViewType.Estimation -> EstimationHolder(layoutInflater, parent, estimateActionListener)
            ViewType.EstimationWithBox -> EstimationWithBoxHolder(layoutInflater, parent)
            ViewType.Feedback -> FeedbackHolder(layoutInflater, parent)
            ViewType.Submit -> SubmitHolder(layoutInflater, parent)
        }

    override fun onBindViewHolder(holder: RowHolder, position: Int): Unit {
        holder.bind(items[position])
    }

    override fun getItemViewType(position: Int): Int = items[position].viewType.ordinal

    override fun getItemCount(): Int = items.size

    abstract class RowHolder(v: View) : RecyclerView.ViewHolder(v) {
        abstract fun bind(cell: MyCell)
    }

    class EstimationHolder(private val v: View) :
        RowHolder(v) {

        private var questionTextView: TextView = v.questionTextView
        private var ratingBar: RatingBar = v.ratingBar
        private var estimateActionListener: EstimateActionListener? = null

        constructor(
            layoutInflater: LayoutInflater,
            parent: ViewGroup,
            listener: ViewTypeActionListener
        ) : this(layoutInflater.inflate(R.layout.item_estimation, parent, false)) {
            if (listener is EstimateActionListener) {
                estimateActionListener = listener
            }
        }

        override fun bind(cell: MyCell) {
            if (cell is MyCell.Estimation) {
                questionTextView.text = cell.questionText
                ratingBar.numStars = 5
                ratingBar.rating = cell.estimationNum.toFloat()
                ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                    estimateActionListener?.onRatingChanged(rating.toInt(), adapterPosition)
                }
            }
        }
    }

    class EstimationWithBoxHolder(private val v: View) :
        RowHolder(v) {

        private var questionTextView: TextView = v.questionTextView2
        private var ratingBar: RatingBar = v.ratingBar2
        private var radioButton: RadioButton = v.radioButton

        constructor(
            layoutInflater: LayoutInflater,
            parent: ViewGroup
        ) : this(layoutInflater.inflate(R.layout.item_estimation_with_box, parent, false))

        override fun bind(cell: MyCell) {
            if (cell is MyCell.EstimationWithBox) {
                questionTextView.text = cell.questionText
                ratingBar.numStars = 5
                ratingBar.rating = cell.estimationNum.toFloat()
                radioButton.text = cell.boxText
            }
        }
    }

    class FeedbackHolder(private val v: View) :
        RowHolder(v) {

        private var feedbackTitleTextView = v.feedbackTitleTextView
        private var feedbackEditText = v.feedbackEditText

        constructor(
            layoutInflater: LayoutInflater,
            parent: ViewGroup
        ) : this(layoutInflater.inflate(R.layout.item_feedback, parent, false))

        override fun bind(cell: MyCell) {
            if (cell is MyCell.Feedback) {
                feedbackTitleTextView.text = cell.titleText
            }
        }
    }

    class SubmitHolder(private val v: View) :
        RowHolder(v) {

        private val submitButton: Button = v.submitButton

        constructor(
            layoutInflater: LayoutInflater,
            parent: ViewGroup
        ) : this(layoutInflater.inflate(R.layout.item_submit, parent, false))

        override fun bind(cell: MyCell) {
            if (cell is MyCell.Submit) {
                submitButton.text = cell.buttonText
            }
        }
    }

    interface ViewTypeActionListener

    interface EstimateActionListener : ViewTypeActionListener {
        fun onRatingChanged(progress: Int, position: Int)
    }
}