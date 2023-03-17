package com.azimsiddiqui.countrylistapp.data

import com.azimsiddiqui.countrylistapp.data.model.CountryDto
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("countries/")
    suspend fun getCountries():Response<CountryDto>
}