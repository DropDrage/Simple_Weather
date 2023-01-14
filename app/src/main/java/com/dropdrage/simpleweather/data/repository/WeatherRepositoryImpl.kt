package com.dropdrage.simpleweather.data.repository

import android.util.Log
import com.dropdrage.simpleweather.data.preferences.WeatherUnitsPreferences
import com.dropdrage.simpleweather.data.source.local.cache.dao.DayWeatherDao
import com.dropdrage.simpleweather.data.source.local.cache.dao.LocationDao
import com.dropdrage.simpleweather.data.source.local.cache.dao.WeatherCacheDao
import com.dropdrage.simpleweather.data.source.local.cache.model.LocationModel
import com.dropdrage.simpleweather.data.source.local.util.LocalResource
import com.dropdrage.simpleweather.data.source.local.util.mapper.toDayModels
import com.dropdrage.simpleweather.data.source.local.util.mapper.toDomainWeather
import com.dropdrage.simpleweather.data.source.local.util.mapper.toHourModels
import com.dropdrage.simpleweather.data.source.local.util.mapper.toNewModel
import com.dropdrage.simpleweather.data.source.remote.WeatherApi
import com.dropdrage.simpleweather.data.util.LogTags
import com.dropdrage.simpleweather.data.util.mapper.toDomainWeather
import com.dropdrage.simpleweather.domain.location.Location
import com.dropdrage.simpleweather.domain.util.Resource
import com.dropdrage.simpleweather.domain.weather.Weather
import com.dropdrage.simpleweather.domain.weather.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val locationDao: LocationDao,
    private val dayWeatherDao: DayWeatherDao,
    private val weatherCacheDao: WeatherCacheDao,
) : CachedRepository(LogTags.WEATHER), WeatherRepository {

    override suspend fun getWeatherFromNow(location: Location): Flow<Resource<Weather>> = flow<Resource<Weather>> {
        val savedLocationModel = locationDao.getLocationApproximately(location.latitude, location.longitude)
        val localWeatherResult = getLocalWeatherResult(savedLocationModel)
        if (localWeatherResult is LocalResource.Success) {
            emit(Resource.Success(localWeatherResult.data))
        }

        if (isUpdateNotRequired(savedLocationModel?.updateTime)) {
            currentCoroutineContext().cancel()
            return@flow
        }

        tryProcessRemoteResourceOrEmitError(localWeatherResult) {
            updateLocalWeatherFromApi(location, savedLocationModel?.id)
            emitUpdatedLocalWeather(savedLocationModel, location)
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun getLocalWeatherResult(locationModel: LocationModel?): LocalResource<Weather> {
        if (locationModel == null) return LocalResource.NotFound()

        val localWeather = dayWeatherDao.getWeatherForLocationFromDay(locationModel.id!!, LocalDate.now())

        return if (localWeather.isNotEmpty()) LocalResource.Success(localWeather.toDomainWeather())
        else LocalResource.NotFound()
    }

    private fun isUpdateNotRequired(updateTime: LocalDateTime?): Boolean =
        updateTime != null && ChronoUnit.HOURS.between(updateTime, LocalDateTime.now()) < 1

    private suspend fun updateLocalWeatherFromApi(location: Location, saveLocationId: Long?) {
        val remoteDomainWeather = api.getWeather(
            location.latitude, location.longitude,
            WeatherUnitsPreferences.temperatureUnit,
            WeatherUnitsPreferences.windSpeedUnit,
            WeatherUnitsPreferences.precipitationUnit,
            TimeZone.getDefault().id.toString(),
        ).toDomainWeather()

        saveNewWeather(location, saveLocationId, remoteDomainWeather)
    }

    private suspend fun saveNewWeather(location: Location, savedLocationId: Long?, remoteDomainWeather: Weather) {
        val locationId =
            if (savedLocationId == null) locationDao.insertAndGetId(location.toNewModel())
            else savedLocationId

        weatherCacheDao.updateWeather(locationId, remoteDomainWeather.toDayModels(locationId)) { daysIds ->
            remoteDomainWeather.toHourModels(daysIds)
        }
    }

    private suspend fun FlowCollector<Resource<Weather>>.emitUpdatedLocalWeather(
        savedLocationModel: LocationModel?,
        location: Location,
    ) {
        val locationModel: LocationModel =
            if (savedLocationModel == null)
                locationDao.getLocationApproximately(location.latitude, location.longitude)!!
            else savedLocationModel

        val updatedLocalWeatherResult = getLocalWeatherResult(locationModel)
        if (updatedLocalWeatherResult is LocalResource.Success) {
            emit(Resource.Success(updatedLocalWeatherResult.data))
        } else {
            Log.e(LogTags.WEATHER, "Can't obtain resource after save: $locationModel")
            emit(Resource.CantObtainResource())
        }
    }


    override suspend fun updateWeather(location: Location) {
        val savedLocationModel = locationDao.getLocationApproximately(location.latitude, location.longitude)
        if (isUpdateNotRequired(savedLocationModel?.updateTime)) {
            return
        }

        updateLocalWeatherFromApi(location, savedLocationModel?.id)
    }

}
