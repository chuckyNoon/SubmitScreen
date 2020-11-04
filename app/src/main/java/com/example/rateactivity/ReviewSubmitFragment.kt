package com.example.rateactivity

import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalStateException


class ReviewSubmitFragment : Fragment() {

    companion object {
        fun newInstance() = ReviewSubmitFragment()

        const val ERROR_TAG = "error"
    }

    private lateinit var viewModel: ReviewSubmitViewModel

    private var toolbar: androidx.appcompat.widget.Toolbar? = null
    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var headerTitleView: TextView? = null
    private var headerSecondLineView: TextView? = null
    private var headerThirdLineView: TextView? = null
    private var headerRatingBar: RatingBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val notNullActivity = requireActivity()
            val viewModelFactory = ReviewSubmitViewModelFactory(
                HeaderStateSource(notNullActivity.application),
                CellStatesSource(notNullActivity.application)
            )
            viewModel = ViewModelProvider(notNullActivity, viewModelFactory).get(
                ReviewSubmitViewModel::class.java
            )
        } catch (ex: IllegalStateException) {
            Log.e(ERROR_TAG, ex.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_review_submit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFields(view)
        setupToolBar()
        setupHeader()
        setupRecyclerView()
    }

    private fun initFields(v: View) {
        toolbar = v.findViewById(R.id.toolbar)
        recyclerView = v.findViewById(R.id.recyclerView)
        progressBar = v.findViewById(R.id.progressBar)
        headerTitleView = v.findViewById(R.id.headerTitleTextView)
        headerSecondLineView = v.findViewById(R.id.headerSecondLineTextView)
        headerThirdLineView = v.findViewById(R.id.headerThirdLineTextView)
        headerRatingBar = v.findViewById(R.id.headerRatingBar)
    }

    private fun setupToolBar() {
        toolbar?.setNavigationIcon(R.drawable.ic_close_12dp)
        toolbar?.title = ""
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    private fun setupHeader() {
        viewModel.headerViewState.observe(viewLifecycleOwner, { headerViewState ->
            headerTitleView?.text = headerViewState.title
            headerSecondLineView?.text = headerViewState.secondLine
            headerThirdLineView?.text = headerViewState.thirdLine
            headerRatingBar?.rating = headerViewState.rating.toFloat()
        })

        headerRatingBar?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            viewModel.onHeaderRatingChanged(rating.toInt())
        }
    }

    private fun setupRecyclerView() {
        recyclerView?.let { recyclerView ->
            val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            recyclerView.layoutManager = layoutManager

            viewModel.cells.observe(viewLifecycleOwner, { cells ->
                if (cells == null) {
                    return@observe
                }
                val adapter = recyclerView.adapter
                if (adapter == null) {
                    val itemsAdapter =
                        ReviewCellsAdapter(
                            layoutInflater,
                            surveyWithStarIconsInteraction,
                            surveyWithPersonIconsInteraction,
                            surveyWithOptionInteraction,
                            feedbackInteraction,
                            submitInteraction
                        )
                    itemsAdapter.submitCells(cells)
                    recyclerView.adapter = itemsAdapter
                } else {
                    val itemsAdapter = adapter as ReviewCellsAdapter
                    itemsAdapter.submitCells(cells)
                }
            })
        }
    }

    private val surveyWithStarIconsInteraction =
        object : ReviewCellsAdapter.SurveyWithStarIconsInteraction {
            override fun onRatingChanged(rating: Int, position: Int) {
                viewModel.onCellRatingChanged(rating, position)
            }
        }

    private val surveyWithPersonIconsInteraction =
        object : ReviewCellsAdapter.SurveyWithPersonIconsInteraction {
            override fun onRatingChanged(rating: Int, position: Int) {
                viewModel.onCellRatingChanged(rating, position)
            }
        }

    private val surveyWithOptionInteraction =
        object : ReviewCellsAdapter.SurveyWithOptionInteraction {
            override fun onRatingChanged(rating: Int, position: Int) {
                viewModel.onCellRatingChanged(rating, position)
            }

            override fun onAltOptionClicked(position: Int) {
                viewModel.onAltOptionButtonClicked(position)
            }
        }

    private val feedbackInteraction = object : ReviewCellsAdapter.FeedbackInteraction {
        override fun onTextChanged(text: String, position: Int) {
            viewModel.onFeedbackTextChanged(text, position)
        }
    }

    private val submitInteraction = object : ReviewCellsAdapter.SubmitInteraction {
        override fun onSubmitButtonClicked(v: View) {
            if (v.background is TransitionDrawable) {
                val background = v.background as TransitionDrawable
                background.startTransition(100)
                background.reverseTransition(100)
            }
            progressBar?.let { progressBar ->
                progressBar.visibility = View.VISIBLE
                Handler().postDelayed({
                    progressBar.visibility = View.INVISIBLE
                    val text = viewModel.getReport()
                    Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
                }, 1500)
            }
        }
    }
}