package com.azimsiddiqui.countrylistapp.domain.usecase

import com.azimsiddiqui.countrylistapp.domain.repository.CountryRepository
import com.azimsiddiqui.countrylistapp.util.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/*
* This can be another usecase if we want to get the cityname using country name.
* but it requires another API call. so due to this I have not use this as of Now.
* */

class GetCitiesByCountryNameUseCase @Inject constructor(private val countryRepository: CountryRepository) {

    operator fun invoke(country: String): Flow<ApiResult<List<String>>> {
        return flow {
            emit(ApiResult.Loading(""))
            try {
                emit(ApiResult.Success(countryRepository.getCityList(country)))
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message))
            }
        }
    }
}