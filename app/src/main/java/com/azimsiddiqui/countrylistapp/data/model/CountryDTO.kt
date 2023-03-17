package com.azimsiddiqui.countrylistapp.data.model

data class CountryDto(
    val data: List<CountryDetailDTO>?,
    val error: Boolean?,
    val msg: String?
)