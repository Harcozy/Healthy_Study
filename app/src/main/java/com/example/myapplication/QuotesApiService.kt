package com.example.myapplication

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers


interface QuotesApiService {
    @Headers("X-Api-Key:3vV5cpjHGA3WSZFgxmSe6Q==tYdyLlXgiyCPdMNA")
    @GET("quotes?category=education")
    suspend fun getEducationQuotes(): Response<List<Quote>>
}

data class Quote(
    val quote: String,
    val author: String?
)