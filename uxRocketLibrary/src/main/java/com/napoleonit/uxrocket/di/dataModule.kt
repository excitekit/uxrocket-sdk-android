package com.napoleonit.uxrocket.di

import android.content.Context
import android.content.SharedPreferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.napoleonit.uxrocket.data.api.UXRocketApi
import com.napoleonit.uxrocket.data.cache.globalCaching.ITaskCaching
import com.napoleonit.uxrocket.data.cache.globalCaching.TaskCachingImpl
import com.napoleonit.uxrocket.data.repository.uxRocketRepository.IUXRocketRepository
import com.napoleonit.uxrocket.data.repository.uxRocketRepository.UXRocketRepositoryImpl
import com.napoleonit.uxrocket.data.repository.paramsRepository.ParamsRepositoryImpl
import com.napoleonit.uxrocket.data.repository.paramsRepository.IParamsRepository
import com.napoleonit.uxrocket.data.cache.sessionCaching.IMetaInfo
import com.napoleonit.uxrocket.data.cache.sessionCaching.MetaInfo
import com.napoleonit.uxrocket.data.useCases.SaveAppParamsUseCase
import com.napoleonit.uxrocket.data.useCases.CachingParamsUseCase
import com.napoleonit.uxrocket.data.useCases.GetParamsUseCase
import com.napoleonit.uxrocket.data.useCases.SaveAppParamsFromCacheUseCase
import com.napoleonit.uxrocket.shared.NetworkState
import com.napoleonit.uxrocket.shared.UXRocketServer
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private const val CONTENT_TYPE = "application/json"
private const val TASK_PREFERENCE = "TaskPreference"

fun getDataModule(appContext: Context, authKey: String, appRocketId: String, serverEnvironment: UXRocketServer) = module {
    /**Base component's*/
    single { provideJson() }

    single { providePreference(appContext) }

    single { provideTaskCaching(get(), get()) }

    single { provideCachingParams() }

    single { provideMetaInfo(appContext, authKey, appRocketId) }

    single { provideNetworkState(appContext) }

    single { provideInterceptor() }

    single { provideOkHttp(get()) }

    single { provideRetrofit(get(), get(), serverEnvironment) }

    // /**Dao*/
    // single { get<UXRocketDataBase>().crmDao() }

    /**Api's*/
    single<UXRocketApi> { get<Retrofit>().create(UXRocketApi::class.java) }

    /**Repository's*/
    single<IUXRocketRepository> { UXRocketRepositoryImpl(get()) }

    /**Use case's*/
    single { SaveAppParamsUseCase(get(), get()) }
    single { SaveAppParamsFromCacheUseCase(get(), get()) }
    single { CachingParamsUseCase(get()) }
    single { GetParamsUseCase(get()) }
}

fun provideNetworkState(appContext: Context) = NetworkState(appContext)

fun provideCachingParams(): IParamsRepository =
    ParamsRepositoryImpl()

fun provideTaskCaching(sharedPreferences: SharedPreferences, json: Json): ITaskCaching =
    TaskCachingImpl(sharedPreferences, json)

fun providePreference(appContext: Context): SharedPreferences {
    return appContext.getSharedPreferences(TASK_PREFERENCE, Context.MODE_PRIVATE)
}

fun provideMetaInfo(appContext: Context, authKey: String, appRocketId: String): IMetaInfo = MetaInfo(appContext, authKey, appRocketId)

private fun provideRetrofit(okHttpClient: OkHttpClient, json: Json, serverEnvironment: UXRocketServer) =
    Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(serverEnvironment.serverUrl)
        .addConverterFactory(json.asConverterFactory(CONTENT_TYPE.toMediaType()))
        .build()


private fun provideJson() = Json {
    isLenient = true
    coerceInputValues = true
    ignoreUnknownKeys = true
}

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