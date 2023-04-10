# Simple Weather

Simple Weather app based on [Open Meteo](https://open-meteo.com/). Used for library tests (Cicerone, Compose, Ktor).  
Uses your current approximate location to get the weather or you can add cities manually to monitor them.

[Design](https://www.figma.com/file/k7czEQd3tDVYE8iBou7A4l/weather-app?node-id=0%3A1&t=Y1pbLyvAuVDB6FDq-0) by [Oleg Buchinskiy](https://www.behance.net/olegbuchinskiy)  
Weather Request [example](https://api.open-meteo.com/v1/forecast?latitude=53&longitude=13&hourly=temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl,visibility&daily=weathercode,temperature_2m_min,temperature_2m_max,apparent_temperature_min,apparent_temperature_max,precipitation_sum,windspeed_10m_max,sunrise,sunset&timezone=UTC)

### Techs

#### Main

**Architecture**: Clean Architecture, Multi-module  
**Presentation**: MVVM  
**Main Techs**: Retrofit (with Moshi), Dagger Hilt, Android Jetpack  
**Additional Techs**: Kotpref, ViewBindingPropertyDelegate  
**Tests**: JUnit 5, Mockk, Turbine

#### Library tests

- [Cicerone](https://github.com/DropDrage/Simple_Weather/tree/library-swap/SW-20-cicerone)
- [Compose](https://github.com/DropDrage/Simple_Weather/tree/library-swap/SW-21-jetpack-compose)
