package com.dropdrage.simpleweather.data.repository

import android.util.Log
import com.dropdrage.simpleweather.data.preferences.WeatherUnitsPreferences
import com.dropdrage.simpleweather.data.source.local.cache.dao.DayWeatherDao
import com.dropdrage.simpleweather.data.source.local.cache.dao.HourWeatherDao
import com.dropdrage.simpleweather.data.source.local.cache.dao.LocationDao
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val locationDao: LocationDao,
    private val dayWeatherDao: DayWeatherDao,
    private val hourWeatherDao: HourWeatherDao,
) : CachedRepository(LogTags.WEATHER), WeatherRepository {

    override suspend fun getWeatherFromNow(location: Location): Flow<Resource<Weather>> = flow {
        val savedLocationModel = locationDao.getLocationApproximately(location.latitude, location.longitude)
        val localWeatherResult = getLocalWeatherResult(savedLocationModel)
        if (localWeatherResult is LocalResource.Success) {
            emit(Resource.Success(localWeatherResult.data))
        }

        tryProcessRemoteResourceOrEmitError(localWeatherResult) {
            updateLocalWeatherFromApi(location, savedLocationModel?.id)
            emitUpdatedLocalWeather(savedLocationModel, location)
        }
    }

    private suspend fun getLocalWeatherResult(locationModel: LocationModel?): LocalResource<Weather> {
        if (locationModel == null) return LocalResource.NotFound()

        val localWeather = dayWeatherDao.getWeatherForLocationFromDay(locationModel.id!!, LocalDate.now())

        return if (localWeather.isNotEmpty()) LocalResource.Success(localWeather.toDomainWeather())
        else LocalResource.NotFound()
    }

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

        val daysIds = dayWeatherDao.clearAndInsertWeather(locationId, remoteDomainWeather.toDayModels(locationId))
        hourWeatherDao.insertAll(remoteDomainWeather.toHourModels(daysIds))
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
            Log.e(LogTags.WEATHER, "Can't obtain resource after save")
            emit(Resource.CantObtainResource())
        }
    }

}
