package com.dropdrage.simpleweather.data.weather.di

import com.dropdrage.simpleweather.data.settings.util.ApiSupportedParam
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

internal class ApiSupportedParamFactory : Converter.Factory() {

    private val supportedType = ApiSupportedParam::class.java


    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<*, String>? {
        if ((type as Class<*>).interfaces.contains(supportedType)) {
            return ApiSupportedParamConverter
        }
        return super.stringConverter(type, annotations, retrofit)
    }


    object ApiSupportedParamConverter : Converter<ApiSupportedParam, String> {
        override fun convert(value: ApiSupportedParam): String = value.apiParam
    }

}
