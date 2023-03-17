package com.azimsiddiqui.countrylistapp.data.model

data class CountryModel(
    val countryList: List<CountryDetail>,
    val error: Boolean,
    val msg: String
)