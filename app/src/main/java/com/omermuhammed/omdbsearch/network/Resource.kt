package com.omermuhammed.omdbsearch.network

// Represents a network bound resource. Each subclass represents the resource's state:
// - [Loading]: the resource is being retrieved from network.
// - [Success]: the resource has been retrieved (available in [Success.data] field)
// - [Failure]: the resource retrieving has failed
sealed class Resource<out T> {

    class Loading<out T> : Resource<T>()

    data class Success<out T>(val data: T) : Resource<T>()

    data class Failure<out T>(val errorMessage: String) : Resource<T>()
}