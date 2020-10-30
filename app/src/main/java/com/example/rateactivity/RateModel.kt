package com.example.rateactivity

object RateModel {
    fun getCells() : ArrayList<MyCell>{
        val cells = ArrayList<MyCell>()
        cells.add(MyCell.Estimation("How crowded was the flight?", null, 0))
        cells.add(MyCell.Estimation("How do you rate the aircraft?", null, 0))
        cells.add(MyCell.Estimation("How do you rate the seats?", null, 0))
        cells.add(MyCell.Estimation("How do you rate the crew?", null, 0))
        cells.add(
            MyCell.EstimationWithBox(
                "How do you rate the food?",
                null,
                0,
                "There were no food"
            )
        )
        cells.add(MyCell.Feedback("Leave any feedback"))
        cells.add(MyCell.Submit("Submit"))
        return cells
    }
}