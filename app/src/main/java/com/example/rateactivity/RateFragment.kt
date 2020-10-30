package com.example.rateactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RateFragment : Fragment() {

    companion object {
        fun newInstance() = RateFragment()
    }

    private val viewModel by lazy { ViewModelProvider(this).get(RateViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rate_fragment_motion_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        viewModel.cells.observe(viewLifecycleOwner, Observer<List<MyCell>>() { cells ->
            if (cells == null) {
                return@Observer;
            }
            val adapter = recyclerView.adapter;
            if (adapter == null) {
                val itemsAdapter =
                    ItemsAdapter(layoutInflater, ArrayList(cells), estimateActionListener)
                recyclerView.adapter = itemsAdapter
            }
        })
    }

    private val estimateActionListener = object : ItemsAdapter.EstimateActionListener {
        override fun onRatingChanged(progress: Int, position: Int) {
            viewModel.updateRating(progress, position)
        }
    }
}