package com.example.rateactivity

import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ReviewSubmitFragment : Fragment() {

    companion object {
        fun newInstance() = ReviewSubmitFragment()
    }

    private lateinit var viewModel: ReviewSubmitViewModel

    private var motionLayout: MotionLayout? = null
    private var toolbar: androidx.appcompat.widget.Toolbar? = null
    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var headerTitleView: TextView? = null
    private var headerSecondLineView: TextView? = null
    private var headerThirdLineView: TextView? = null
    private var headerRatingBar: RatingBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val parentActivity = requireActivity()
        val viewModelFactory = ReviewSubmitViewModelFactory(
            HeaderStateSource(parentActivity.application),
            CellStatesSource(parentActivity.application)
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(
            ReviewSubmitViewModel::class.java
        )
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
        setupMotionLayout()
        setupRecyclerView()
        setupProgressBar()
        setupToastMaker()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop(motionLayout?.progress)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        motionLayout = null
        toolbar = null
        recyclerView = null
        progressBar = null
        headerTitleView = null
        headerSecondLineView = null
        headerThirdLineView = null
        headerRatingBar = null
    }

    private fun initFields(v: View) {
        progressBar = v.findViewById(R.id.progressBar)
        motionLayout = v.findViewById(R.id.motionLayout)
        toolbar = v.findViewById(R.id.toolbar)
        recyclerView = v.findViewById(R.id.recyclerView)
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
        viewModel.headerViewState.observe(viewLifecycleOwner, Observer { headerViewState ->
            if (headerViewState == null) {
                return@Observer
            }
            headerTitleView?.text = headerViewState.title
            headerSecondLineView?.text = headerViewState.secondLine
            headerThirdLineView?.text = headerViewState.thirdLine
            headerRatingBar?.rating = headerViewState.rating.toFloat()
            headerRatingBar?.setIsIndicator(!headerViewState.isClickable)
        })

        headerRatingBar?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            viewModel.onHeaderRatingChanged(rating.toInt())
        }
    }

    private fun setupRecyclerView() {
        recyclerView?.let { recyclerView ->
            val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            recyclerView.layoutManager = layoutManager

            viewModel.cells.observe(viewLifecycleOwner, Observer { cells ->
                if (cells == null) {
                    return@Observer
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

    private fun setupMotionLayout() {
        viewModel.motionLayoutProgress.observe(viewLifecycleOwner, Observer { progress ->
            if (progress == null) {
                return@Observer
            }
            if (motionLayout?.progress != progress)
                motionLayout?.progress = progress
        })
    }

    private fun setupToastMaker() {
        viewModel.toast.observe(viewLifecycleOwner, Observer { toastMessage ->
            if (toastMessage == null) {
                return@Observer
            }
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
        })
    }

    private fun setupProgressBar() {
        viewModel.isProgressBarVisible.observe(viewLifecycleOwner, Observer { isProgressBarVisible ->
            if (isProgressBarVisible == null) {
                return@Observer
            }
            progressBar?.visibility = if (isProgressBarVisible) View.VISIBLE else View.INVISIBLE
        })
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
            viewModel.onSubmitButtonClicked()
        }
    }
}
