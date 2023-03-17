package com.azimsiddiqui.countrylistapp.data.repository

import com.azimsiddiqui.countrylistapp.common.toDomain
import com.azimsiddiqui.countrylistapp.data.ApiService
import com.azimsiddiqui.countrylistapp.data.model.CountryDetail
import com.azimsiddiqui.countrylistapp.domain.repository.CountryRepository
import com.azimsiddiqui.countrylistapp.util.SafeApiRequest
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    CountryRepository,
    SafeApiRequest() {
    override suspend fun getCountryList(): List<CountryDetail> {
        val countryList = safeApiRequest {
            apiService.getCountries()
        }.toDomain().countryList
        return countryList
    }

    override suspend fun getCityList(countryName: String): List<String> {
        val cityList = safeApiRequest {
            apiService.getCountries()
        }.toDomain().countryList.find { it.country == countryName }?.cities ?: listOf()
        return cityList
    }
}