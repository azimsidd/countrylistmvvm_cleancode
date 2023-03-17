package com.azimsiddiqui.countrylistapp.domain.usecase

import android.util.Log
import com.azimsiddiqui.countrylistapp.data.model.CountryDetail
import com.azimsiddiqui.countrylistapp.domain.repository.CountryRepository
import com.azimsiddiqui.countrylistapp.util.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCountryDataUseCase @Inject constructor(private val countryRepository: CountryRepository) {

    operator fun invoke(): Flow<ApiResult<List<CountryDetail>>> {
        return flow {
            emit(ApiResult.Loading(""))
            try {
                emit(ApiResult.Success(countryRepository.getCountryList()))
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message))
            }
        }
    }

}

