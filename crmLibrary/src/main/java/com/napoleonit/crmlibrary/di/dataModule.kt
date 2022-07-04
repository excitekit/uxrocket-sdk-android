@file:OptIn(ExperimentalSerializationApi::class)

package com.napoleonit.crmlibrary.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.napoleonit.crmlibrary.BuildConfig
import com.napoleonit.crmlibrary.data.api.UXRocketApi
import com.napoleonit.crmlibrary.data.db.UXRocketDataBase
import com.napoleonit.crmlibrary.data.db.dao.UXRocketDao
import com.napoleonit.crmlibrary.data.repository.IUXRocketRepository
import com.napoleonit.crmlibrary.data.repository.UXRocketRepositoryImpl
import com.napoleonit.crmlibrary.data.useCases.InitUseCase
import com.napoleonit.crmlibrary.data.useCases.SaveAppParamsUseCase
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private const val CONTENT_TYPE = "application/json"
private const val BASE_URL = "https://apidev.uxrocket.ru"

@ExperimentalSerializationApi
fun getDataModule(appContext: Context) = module {
    /**Base component's*/
    single { provideJson() }

    single { provideDataBase(appContext) }

    single { provideInterceptor() }

    single { provideOkHttp(get<Interceptor>()) }

    single { provideRetrofit(get<OkHttpClient>(), get<Json>()) }

    /**Dao*/
    single<UXRocketDao> { get<UXRocketDataBase>().crmDao() }

    /**Api's*/
    single<UXRocketApi> { get<Retrofit>().create(UXRocketApi::class.java) }

    /**Repository's*/
    single<IUXRocketRepository> { UXRocketRepositoryImpl(get<UXRocketApi>(), get<UXRocketDao>()) }

    /**Use case's*/
    single<InitUseCase> { InitUseCase(get<IUXRocketRepository>()) }
    single<SaveAppParamsUseCase> { SaveAppParamsUseCase(get<IUXRocketRepository>()) }
}

private fun provideRetrofit(okHttpClient: OkHttpClient, json: Json) =
    Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory(CONTENT_TYPE.toMediaType()))
        .build()


private fun provideJson() = Json {
    isLenient = true
    coerceInputValues = true
    ignoreUnknownKeys = true
}

@ExperimentalSerializationApi
private fun provideDataBase(appContext: Context) = Room.databaseBuilder(
    appContext, UXRocketDataBase::class.java, "get_crm_db"
).build()

private fun provideInterceptor() = Interceptor {
    val request = it.request()
        .newBuilder()
        .addHeader("Content-Type", CONTENT_TYPE)
    return@Interceptor it.proceed(request.build())
}

private fun provideOkHttp(interceptor: Interceptor): OkHttpClient {
    val builder = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
    if (BuildConfig.DEBUG) {
        builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    } else {
        builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
    }

    return builder.build()
}