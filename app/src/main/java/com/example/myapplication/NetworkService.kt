// Package name
package com.example.myapplication

//Import Statements
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// This is the declaration of NetworkService object
// It's an object declaration, which is a singleton class in Kotlin: There's only one instance of this class in the entire app.
object NetworkService {
    // This is the base URL for the API
    private const val BASE_URL = "https://api.api-ninjas.com/v1/"

    // This is the Retrofit object that will be used to make API calls
    // Retrofit is a type-safe HTTP client for Android and Java
    // It's configured with the base URL of the API and a Gson converter factory, which will be used to convert the JSON responses from API into Kotlin objects.
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // This is a lazy property that holds a reference to your QuotesApiService.
    // The QuotesApiService is created by the Retrofit instance.
    val quotesApi: QuotesApiService by lazy { // The 'by lazy' means that the QuotesApiService will only be created when it's first accessed.

        retrofit.create(QuotesApiService::class.java)
    }
}