package com.dropdrage.simpleweather.data.city.remote

import com.dropdrage.simpleweather.city.domain.City
import com.dropdrage.simpleweather.city.domain.Country
import com.dropdrage.simpleweather.core.domain.Location
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class CitiesMapperTest {

    @Test
    fun `CitiesDto toDomainCities`() {
        val cityDtos = listOf(
            CityDto("City", 1f, 2f, "CY", "Country"),
            CityDto("City2", 13f, 2f, "CY2", "Country2"),
            CityDto("City3", 11f, 22f, "CY1", "Country1"),
        )
        val dto = CitiesDto(cityDtos)
        val cityDomains = cityDtos.map(::toDomainCity)

        val result = dto.toDomainCities()

        assertThat(result).containsExactlyElementsIn(cityDomains)
    }

    private fun toDomainCity(dto: CityDto): City = City(
        name = dto.name,
        location = Location(dto.latitude, dto.longitude),
        country = Country(dto.country, dto.countryCode),
    )

}
