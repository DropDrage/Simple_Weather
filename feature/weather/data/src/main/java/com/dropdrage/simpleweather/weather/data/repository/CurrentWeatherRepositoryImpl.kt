package com.dropdrage.simpleweather.weather.data.repository

import android.util.Log
import com.dropdrage.simpleweather.city_list.domain.weather.CurrentWeather
import com.dropdrage.simpleweather.city_list.domain.weather.CurrentWeatherRepository
import com.dropdrage.simpleweather.common.data.LocalResource
import com.dropdrage.simpleweather.core.data.LogTags
import com.dropdrage.simpleweather.core.domain.Location
import com.dropdrage.simpleweather.data.settings.WeatherUnitsPreferences
import com.dropdrage.simpleweather.data.weather.remote.toDomain
import com.dropdrage.simpleweather.data.weather.remote.toDomainCurrentWeather
import com.dropdrage.simpleweather.weather.data.local.cache.dao.HourWeatherDao
import com.dropdrage.simpleweather.weather.data.local.cache.dao.LocationDao
import com.dropdrage.simpleweather.weather.data.local.cache.model.LocationModel
import com.dropdrage.simpleweather.weather.data.local.util.LocalDateTimeUtils
import com.dropdrage.simpleweather.weather.data.remote.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.net.UnknownHostException
import javax.inject.Inject

class CurrentWeatherRepositoryImpl internal @Inject constructor(
    private val api: WeatherApi,
    private val locationDao: LocationDao,
    private val hourWeatherDao: HourWeatherDao,
) : CurrentWeatherRepository {

    override suspend fun getCurrentWeather(locations: List<Location>): Flow<List<CurrentWeather?>> = flow {
        val locationsFlow = locations.asFlow()
        val localWeathers = locationsFlow.map(::getLocalCurrentWeather).toList()
        emit(localWeathers)

        try {
            val remoteWeathers = locationsFlow
                .map {
                    api.getCurrentWeather(
                        it.latitude,
                        it.longitude,
                        WeatherUnitsPreferences.temperatureUnit
                    ).toDomainCurrentWeather()
                }
                .toList()
            emit(remoteWeathers)
        } catch (e: UnknownHostException) {
            Log.e(LogTags.WEATHER, "Probably, no internet: " + e.message, e)
        } catch (e: Exception) {
            Log.e(LogTags.WEATHER, e.message, e)
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun getLocalCurrentWeather(location: Location): CurrentWeather? {
        val savedLocationModel = locationDao.getLocationApproximately(location.latitude, location.longitude)
        val localCurrentWeatherResult = getCurrentWeatherResult(savedLocationModel)
        return if (localCurrentWeatherResult is LocalResource.Success) localCurrentWeatherResult.data
        else null
    }

    private suspend fun getCurrentWeatherResult(locationModel: LocationModel?): LocalResource<CurrentWeather> {
        if (locationModel == null) return LocalResource.NotFound()

        val localWeather = hourWeatherDao.getCurrentWeather(locationModel.id!!, LocalDateTimeUtils.nowHourOnly)

        return if (localWeather != null) LocalResource.Success(localWeather.toDomain())
        else LocalResource.NotFound()
    }

}