package com.carelon.whe.util

import com.carelon.whe.domain.model.ErrorItem
import com.carelon.whe.network.Resource
import com.google.gson.JsonSyntaxException
import java.io.EOFException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

class NetworkUtils {

    companion object {
        fun resolveError(e: Exception): Resource.Error {
            val error: ErrorItem = when (e) {
                is EOFException -> {
                    ErrorItem(title = "Error Occurred", message = "No response from server.")
                }
                is SocketTimeoutException -> {
                    ErrorItem(title = "Error Occurred", message = "Server connection timeout.")
                }
                is SSLHandshakeException -> {
                    ErrorItem(title = "Error Occurred", message = "Invalid SSL Certificate.")
                }
                is JsonSyntaxException -> {
                    ErrorItem(
                        title = "Error Occurred",
                        message = "We are not able to process your request right now. Please try again later."
                    )
                }
                is UnknownHostException -> {
                    ErrorItem(
                        title = "Error Occurred",
                        message = "You're currently offline. Please check your internet connection and try again."
                    )
                }
                else -> {
                    ErrorItem(
                        title = "Error Occurred",
                        message = "We are not able to process your request right now. Please try again later."
                    )
                }
            }
            return Resource.Error(error)
        }
    }
}
