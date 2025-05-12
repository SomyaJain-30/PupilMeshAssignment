package com.example.pupilmeshassignment.data.networking

sealed class NetworkCallResponse<T>(
    val data: T? = null,
    val message: String? = null,
    val isCached: Boolean = false
) {
    class Success<T>(data: T, isCached: Boolean = false) :
        NetworkCallResponse<T>(data, isCached = isCached)

    class Error<T>(message: String, data: T? = null) : NetworkCallResponse<T>(data, message)
    class Loading<T> : NetworkCallResponse<T>()
}