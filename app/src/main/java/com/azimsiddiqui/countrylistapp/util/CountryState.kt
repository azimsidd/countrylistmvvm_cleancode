package com.azimsiddiqui.countrylistapp.util

// this useful if we use only flow in place of livedata
data class CountryState(
    val isLoading: Boolean = false,
    val data: List<String>? = null,
    val error: String = ""
)