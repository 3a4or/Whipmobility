package com.ashour.whipmobilitytest.data.entitities

data class LineChart(
    val chartType: String,
    val description: String,
    val items: List<Item>,
    val title: String
)