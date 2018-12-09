package com.cueball.rideasy.api

import com.cueball.rideasy.data.UberRide
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * @author cueo
 */
internal class UberServiceTest {

    private lateinit var uber: UberService
    private val startLatitude: Float = 12.966172F
    private val startLongitude: Float = 77.714975F
    private val endLatitude: Float = 12.926762F
    private val endLongitude: Float = 77.686592F

    @BeforeEach
    fun setUp() {
        uber = UberService()
    }

    @Test
    fun getPriceEstimate() {
        val prices = uber.getPriceEstimates(startLatitude, startLongitude, endLatitude, endLongitude)
        assertTrue(prices!!.stream().anyMatch { item -> "UberGo" == item.displayName })
        assertTrue(prices.stream().anyMatch { item -> "Pool" == item.displayName })
        assertTrue(prices.stream().anyMatch { item -> "UberXL" == item.displayName })
        assertTrue(prices.stream().anyMatch { item -> "Premier" == item.displayName })
        assertTrue(prices.stream().anyMatch { item -> "Pool Airport" == item.displayName })
    }

    @Test
    fun getPickupTimeEstimate() {
        var pickupTimeEstimate = uber.getPickupTimeEstimate(startLatitude, startLongitude, UberRide.POOL)
        assertTrue(pickupTimeEstimate!! > 0)
        pickupTimeEstimate = uber.getPickupTimeEstimate(startLatitude, startLongitude, UberRide.GO)
        assertTrue(pickupTimeEstimate!! > 0)
    }
}
