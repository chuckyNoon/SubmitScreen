package com.example.rateactivity

import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

    private var toolbar: androidx.appcompat.widget.Toolbar? = null
    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(
                requireActivity(),
                ReviewSubmitViewModelFactory(requireActivity().application)
            ).get(
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
        setupRecyclerView()
    }

    private fun initFields(v: View) {
        toolbar = v.findViewById(R.id.toolbar)
        recyclerView = v.findViewById(R.id.recyclerView)
        progressBar = v.findViewById(R.id.progressBar)
    }

    private fun setupToolBar() {
        toolbar?.setNavigationIcon(R.drawable.ic_close_12dp)
        toolbar?.title = ""
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    private fun setupRecyclerView() {
        recyclerView?.let{recyclerView->
            val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            recyclerView.layoutManager = layoutManager

            viewModel.cells.observe(viewLifecycleOwner, Observer<List<ReviewCell>>() { cells ->
                if (cells == null) {
                    return@Observer;
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
               viewModel.onRatingChanged(rating, position)
            }
        }

    private val surveyWithPersonIconsInteraction =
        object : ReviewCellsAdapter.SurveyWithPersonIconsInteraction {
            override fun onRatingChanged(rating: Int, position: Int) {
                viewModel.onRatingChanged(rating, position)
            }
        }

    private val surveyWithOptionInteraction =
        object : ReviewCellsAdapter.SurveyWithOptionInteraction {
            override fun onRatingChanged(rating: Int, position: Int) {
                viewModel.onRatingChanged(rating, position)
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
            if (v.background is TransitionDrawable){
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