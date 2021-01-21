package com.example.traveldeal2.utils

class UserLocation() {
    var lat: Double = 0.0
        private set
    var lon: Double = 0.0
        private set

    constructor(_lat: Double, _lon: Double) : this() {
        lat = _lat
        lon = _lon
    }
}

