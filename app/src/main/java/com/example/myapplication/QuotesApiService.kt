// Package name
package com.example.myapplication

//Import Statements
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

// This is the declaration of the QuotesApiService interface.
// It defines the methods that the app can use to interact with API.
interface QuotesApiService {
    // This is a method that makes a GET request to the "quotes?category=education" endpoint of your API. The @Headers annotation adds a header to the request.
    @Headers("X-Api-Key:iWkmtvccDqkakY27z3F0KgH9XYsRfCoZuSXQ8o3k")
    @GET("quotes?category=inspirational")
    // The suspend keyword means that this method is a suspending function, which can be paused and resumed at a later time.
    // This allows it to perform long-running operations, like network requests, without blocking the main thread.
    suspend fun getEducationQuotes(): Response<List<Quote>>
}

// This is a data class that represents a quote.
// A data class automatically generates useful methods like equals(), hashCode(), and toString().
data class Quote(
    val quote: String, // This is a property that holds the text of the quote.
    val author: String? // This is a property that holds the author of the quote. The question mark means that this property can be null.
)