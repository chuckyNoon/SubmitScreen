package com.example.rateactivity

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
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

        /*requireActivity().window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )*/

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
            viewModel?.let {
               /* val progressBar = view!!.findViewById<ProgressBar>(R.id.progressBar)
                progressBar.visibility = View.VISIBLE
                Handler().postDelayed({
                    progressBar.visibility = View.INVISIBLE
                    val submitState = it.getSubmitState()
                    val reportText = makeReport(submitState)
                    Toast.makeText(requireContext(), reportText, Toast.LENGTH_LONG).show()
                }, 1500)*/
            }
        }
    }

    private fun makeReport(submitState: SubmitState): String {
        return StringBuilder().apply {
            append(makeReportLine("text", submitState.text))
            append(makeReportLine("food", submitState.food.toString()))
            append(makeReportLine("flight", submitState.flight.toString()))
            append(makeReportLine("crew", submitState.crew.toString()))
            append(makeReportLine("aircraft", submitState.aircraft.toString()))
            append(makeReportLine("seat", submitState.seat.toString()))
            append(makeReportLine("people", submitState.people.toString()))
        }.toString()
    }

    private fun makeReportLine(fieldName: String, value: String): String {
        return ("$fieldName = $value\n")
    }

}