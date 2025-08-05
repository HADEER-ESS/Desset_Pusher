package com.hadeer.dessetpusher

data class Dessert(
    val id : Int,
    val image : Int,
    val price : Int,
    val name : String
)

data class Receipt(
    var count : Int,
    val singleCost : Int,
)
