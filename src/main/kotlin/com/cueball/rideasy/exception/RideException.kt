package com.cueball.rideasy.exception

/**
 * @author cueo
 */
class RideException(message: String?) : Exception(message) {

    enum class ExceptionType(val exceptionMessage: String) {
        PRODUCT_NOT_FOUND("No product found for the location")
    }
}
