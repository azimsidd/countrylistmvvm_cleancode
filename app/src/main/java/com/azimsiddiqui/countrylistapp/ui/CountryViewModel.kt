package com.azimsiddiqui.countrylistapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azimsiddiqui.countrylistapp.data.model.CountryDetail
import com.azimsiddiqui.countrylistapp.domain.usecase.GetCountryDataUseCase
import com.azimsiddiqui.countrylistapp.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
*  author Azim Siddiui
* date: 16-03-2023
* */

@HiltViewModel
class CountryViewModel @Inject constructor(
    private val getCountryDataUseCase: GetCountryDataUseCase,
) : ViewModel() {

    private var _countryListLiveData = MutableLiveData<ApiResult<List<String>>>()
    val countryListLiveData: LiveData<ApiResult<List<String>>>
        get() = _countryListLiveData

    private var originalCountryList = listOf<String>()

    private var originalCityList = listOf<String>()

    private var countryDataList = listOf<CountryDetail>()

    private var _cityListLiveData = MutableLiveData<ApiResult<List<String>>>()
    val cityListLiveData: LiveData<ApiResult<List<String>>>
        get() = _cityListLiveData

    // fetch the all country
    fun getCountryList() {
        getCountryDataUseCase.invoke().onEach {
            when (it) {
                is ApiResult.Loading -> {
                    _countryListLiveData.value = ApiResult.Loading("")
                }
                is ApiResult.Success -> {
                    val dataResponse = it.data
                    // store the whole countryList Model into countryDataList
                    countryDataList = dataResponse ?: emptyList()

                    // get list of country name
                    val countryList = dataResponse?.map { it.country } ?: emptyList()
                    _countryListLiveData.value = ApiResult.Success(countryList)
                    originalCountryList = countryList
                }
                is ApiResult.Error -> {
                    _countryListLiveData.value = ApiResult.Error(it.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun filterCountry(query: String) {
        if (query.isNotEmpty()) {
            val filteredList = originalCountryList.filter { it.contains(query, true) }
            _countryListLiveData.value = ApiResult.Success(filteredList)
        } else {
            _countryListLiveData.value = ApiResult.Success(originalCountryList)
        }
    }

    //fetch the all cities
    fun getCityList(country: String) {
        viewModelScope.launch {
            // get list of city name based on country
            val cityList = countryDataList.find { it.country == country }?.cities ?: listOf()
            _cityListLiveData.value = ApiResult.Success(cityList)
            originalCityList = cityList
        }
    }

    fun filterCity(query: String) {
        if (query.isNotEmpty()) {
            val filteredList = originalCityList.filter { it.contains(query, true) }
            _cityListLiveData.value = ApiResult.Success(filteredList)
        } else {
            _cityListLiveData.value = ApiResult.Success(originalCityList)
        }
    }

}