package com.cueball.rideasy.service

import com.cueball.rideasy.api.UberService
import com.cueball.rideasy.data.UberRide
import com.cueball.rideasy.exception.RideException
import com.uber.sdk.rides.client.model.PriceEstimate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashMap


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

        val priceEstimates: MutableMap<UberRide, Pair<Int?, Int?>> = EnumMap(UberRide::class.java)
        for (price in prices) {
            if (enumContains<UberRide>(price.displayName)) {
                priceEstimates[UberRide.valueOf(price.displayName)] = Pair(price.lowEstimate, price.highEstimate)
            }
        }
        return priceEstimates
    }

    private inline fun <reified T: Enum<T>> enumContains(name: String): Boolean {
        return enumValues<T>().any { it.name == name }
    }
}
