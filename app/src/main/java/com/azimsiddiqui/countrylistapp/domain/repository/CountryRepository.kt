package com.azimsiddiqui.countrylistapp.domain.repository

import com.azimsiddiqui.countrylistapp.data.model.CountryDetail
import com.azimsiddiqui.countrylistapp.data.model.CountryModel
import com.azimsiddiqui.countrylistapp.util.ApiResult

interface CountryRepository {
    suspend fun getCountryList(): List<CountryDetail>
    suspend fun getCityList(countryName:String):List<String>
}