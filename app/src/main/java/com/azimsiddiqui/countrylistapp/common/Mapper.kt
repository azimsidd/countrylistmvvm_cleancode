package com.azimsiddiqui.countrylistapp.common

import com.azimsiddiqui.countrylistapp.data.model.CountryDetail
import com.azimsiddiqui.countrylistapp.data.model.CountryDetailDTO
import com.azimsiddiqui.countrylistapp.data.model.CountryDto
import com.azimsiddiqui.countrylistapp.data.model.CountryModel

fun CountryDto.toDomain(): CountryModel {
    return CountryModel(
        countryList = this.data?.map { it.transformIntoModel() } ?: listOf(),
        error = this.error ?: false,
        msg = this.msg ?: ""
    )
}

fun CountryDetailDTO.transformIntoModel(): CountryDetail {
    return CountryDetail(
        cities = this.cities ?: listOf(),
        country = this.country ?: ""
    )
}