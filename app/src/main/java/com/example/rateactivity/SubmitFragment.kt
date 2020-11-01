package com.example.rateactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SubmitFragment : Fragment() {

    companion object {
        fun newInstance() = SubmitFragment()
    }

    private var viewModel: SubmitViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SubmitViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.submit_fragment_motion_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        viewModel?.cells?.observe(viewLifecycleOwner, Observer<List<SubmitListCell>>() { cells ->
            if (cells == null) {
                return@Observer;
            }
            val adapter = recyclerView.adapter
            if (adapter == null) {
                val itemsAdapter =
                    ItemsAdapter(
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
                val itemsAdapter = adapter as ItemsAdapter
                itemsAdapter.submitCells(cells)
            }
        })
    }

    private val surveyWithStarIconsInteraction =
        object : ItemsAdapter.SurveyWithStarIconsInteraction {
            override fun onRatingChanged(rating: Int, position: Int) {
                viewModel?.onEstimateChanged(rating, position)
            }
        }

    private val surveyWithPersonIconsInteraction =
        object : ItemsAdapter.SurveyWithPersonIconsInteraction {
            override fun onRatingChanged(rating: Int, position: Int) {
                viewModel?.onEstimateChanged(rating, position)
            }
        }

    private val surveyWithOptionInteraction =
        object : ItemsAdapter.SurveyWithOptionInteraction {
            override fun onRatingChanged(rating: Int, position: Int) {
                viewModel?.onEstimateChanged(rating, position)
            }

            override fun onAltOptionClicked(position: Int) {
                viewModel?.onAltOptionButtonClicked(position)
            }
        }

    private val feedbackInteraction = object : ItemsAdapter.FeedbackInteraction {
        override fun onTextChanged(text: String, position: Int) {
            viewModel?.onFeedbackTextChanged(text, position)
        }
    }

    private val submitInteraction = object : ItemsAdapter.SubmitInteraction {
        override fun onSubmitButtonClicked() {
            Toast.makeText(requireContext(), "${"123".toString()}", Toast.LENGTH_LONG).show()
        }
    }

}