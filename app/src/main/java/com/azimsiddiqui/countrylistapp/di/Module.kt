package com.azimsiddiqui.countrylistapp.di

import com.azimsiddiqui.countrylistapp.data.ApiService
import com.azimsiddiqui.countrylistapp.data.repository.CountryRepositoryImpl
import com.azimsiddiqui.countrylistapp.domain.repository.CountryRepository
import com.azimsiddiqui.countrylistapp.domain.usecase.GetCitiesByCountryNameUseCase
import com.azimsiddiqui.countrylistapp.domain.usecase.GetCountryDataUseCase
import com.google.gson.Gson
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@dagger.Module
@InstallIn(SingletonComponent::class)
object Module {

    private val okHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(run {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }).build()

    @Provides
    @Singleton
    fun getRetrofitInstance(): Retrofit = Retrofit.Builder().baseUrl(Constant.Base_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun getApiService() = getRetrofitInstance().create(ApiService::class.java)

    @Provides
    @Singleton
    fun getCountryRepository(apiService: ApiService): CountryRepository {
        return CountryRepositoryImpl(apiService)
    }
}