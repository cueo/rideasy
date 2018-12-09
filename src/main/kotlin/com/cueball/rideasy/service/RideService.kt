package com.cueball.rideasy.service

import com.cueball.rideasy.api.UberService
import com.cueball.rideasy.data.UberRide
import com.cueball.rideasy.exception.RideException
import com.uber.sdk.rides.client.model.PriceEstimate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


/**
 * @author cueo
 */
@Service
class RideService {

    @Autowired
    lateinit var uberService: UberService

    fun getUberPriceEstimates(startLatitude: Float, startLongitude: Float, endLatitude: Float, endLongitude: Float): MutableMap<UberRide, Pair<Int?, Int?>> {
        val prices = uberService.getPriceEstimates(startLatitude, startLongitude, endLatitude, endLongitude)
        return filterRides(prices)
    }

    private fun filterRides(prices: List<PriceEstimate>?): MutableMap<UberRide, Pair<Int?, Int?>> {
        if (prices == null) {
            throw RideException(RideException.ExceptionType.PRODUCT_NOT_FOUND.exceptionMessage)
        }

        val priceEstimates: MutableMap<UberRide, Pair<Int?, Int?>> = HashMap()
        for (price in prices) {
            when (price.displayName) {
                UberRide.GO.displayName -> priceEstimates[UberRide.GO] = Pair(price.lowEstimate, price.highEstimate)
                UberRide.POOL.displayName -> priceEstimates[UberRide.POOL] = Pair(price.lowEstimate, price.highEstimate)
                UberRide.XL.displayName -> priceEstimates[UberRide.XL] = Pair(price.lowEstimate, price.highEstimate)
                UberRide.PREMIER.displayName -> priceEstimates[UberRide.PREMIER] = Pair(price.lowEstimate, price.highEstimate)
                UberRide.AIRPORT.displayName -> priceEstimates[UberRide.AIRPORT] = Pair(price.lowEstimate, price.highEstimate)
            }
        }
        return priceEstimates
    }
}
