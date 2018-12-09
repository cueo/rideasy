package com.cueball.rideasy.controller

import com.cueball.rideasy.data.UberRide
import com.cueball.rideasy.service.RideService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author mmayank
 */
@RestController
@RequestMapping("/ride")
class RideController {

    @Autowired
    lateinit var rideService: RideService

    @RequestMapping("/uber-price")
    fun getUberPriceEstimates(
            @RequestParam startLatitude: Float,
            @RequestParam startLongitude: Float,
            @RequestParam endLatitude: Float,
            @RequestParam endLongitude: Float): MutableMap<UberRide, Pair<Int?, Int?>> {
        return rideService.getUberPriceEstimates(startLatitude, startLongitude, endLatitude, endLongitude)
    }
}
