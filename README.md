# Simple Weather
Simple Weather app based on [Open Meteo](https://open-meteo.com/). 
Uses your current approximate location to get the weather.

### Techs
**Architecture**: Clean Architecture, MVVM
**Main Techs**: Retrofit (with Moshi), Dagger Hilt, Android Jetpack
**Additional Techs**: Kotpref, ViewBindingPropertyDelegate

https://api.open-meteo.com/v1/forecast?hourly=temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl&latitude=53&longitude=13