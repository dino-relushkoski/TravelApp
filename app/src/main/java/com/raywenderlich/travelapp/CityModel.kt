package com.raywenderlich.travelapp

import java.util.*

data class CityModel(
    var id: Int = getAutoId(),
    var city: String = "",
    var country: String = "",
    var description: String = ""
) {
    companion object {
        fun getAutoId(): Int {
            val random = Random()
            return random.nextInt(100)
        }
    }
}