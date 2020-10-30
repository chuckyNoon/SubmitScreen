package com.example.rateactivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RateViewModel : ViewModel() {

    private val _cells = MutableLiveData<ArrayList<MyCell>>()
    val cells: LiveData<ArrayList<MyCell>> = _cells

    init{
        _cells.value = RateModel.getCells()
    }

    fun updateRating(rating:Int, position:Int){
        if (_cells.value?.get(position) is MyCell.Estimation){
            val cell = _cells.value?.get(position) as MyCell.Estimation
            cell.estimationNum = rating
        }
    }
}