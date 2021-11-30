package com.codezilla.covid19tracker

import java.util.*

data class CovidTrackerModel ( var id: Int = 1, var name: String=" ", var homeAddress: String=" "){
    //functions, variables here

    companion object{

    }
    fun getAutoId(): Int{
        val random = Random()
        return random.nextInt(100)
    }

}