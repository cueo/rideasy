package com.cueball.rideasy.api

import com.cueball.rideasy.data.UberRide
import com.cueball.rideasy.exception.RideException
import com.uber.sdk.rides.client.ServerTokenSession
import com.uber.sdk.rides.client.SessionConfiguration
import com.uber.sdk.rides.client.UberRidesApi
import com.uber.sdk.rides.client.model.PriceEstimate
import com.uber.sdk.rides.client.model.TimeEstimatesResponse
import com.uber.sdk.rides.client.services.RidesService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * @author cueo
 */
@Service
class UberService(clientId: String = "", serverToken: String = "") {

    @Value("\${data.uber.clientId}")
    private var clientId: String = ""

    @Value("\${uber.serverToken}")
    private var serverToken: String = ""

    private val config: SessionConfiguration = SessionConfiguration.Builder().setClientId(clientId).setServerToken(serverToken).build()

    private val session: ServerTokenSession = ServerTokenSession(config)

    private lateinit var service: RidesService

    init {
        val api: UberRidesApi = UberRidesApi.with(session).build()
        service = api.createService()
    }

    fun getPriceEstimates(startLatitude: Float, startLongitude: Float, endLatitude: Float, endLongitude: Float): MutableList<PriceEstimate>? {
        val priceEstimateCall = service.getPriceEstimates(startLatitude, startLongitude, endLatitude, endLongitude)
        val priceEstimatesResponse = priceEstimateCall.execute()
        return priceEstimatesResponse.body().prices
    }

    private fun getProductId(latitude: Float, longitude: Float, productName: UberRide): String? {
        val products = service.getProducts(latitude, longitude).execute().body().products
        var productId: String? = null
        for (product in products) {
            if (product.displayName == productName.displayName) {
                productId = product.productId
                break
            }
        }
        return productId
    }

    fun getPickupTimeEstimate(startLatitude: Float, startLongitude: Float, productName: UberRide): Int? {
        val productId: String = getProductId(startLatitude, startLongitude, productName) ?: throw RideException(RideException.ExceptionType.PRODUCT_NOT_FOUND.name)
        val pickupTimeEstimateCall = service.getPickupTimeEstimate(startLatitude, startLongitude, productId)
        val pickupTimeEstimateResponse: TimeEstimatesResponse = pickupTimeEstimateCall.execute().body()
        return pickupTimeEstimateResponse.times[0].estimate
    }
}
