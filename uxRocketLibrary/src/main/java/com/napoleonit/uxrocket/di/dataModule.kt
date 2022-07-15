package com.napoleonit.uxrocket.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.napoleonit.uxrocket.BuildConfig
import com.napoleonit.uxrocket.data.api.UXRocketApi
import com.napoleonit.uxrocket.data.db.UXRocketDataBase
import com.napoleonit.uxrocket.data.db.dao.UXRocketDao
import com.napoleonit.uxrocket.data.repository.uxRocketRepository.IUXRocketRepository
import com.napoleonit.uxrocket.data.repository.uxRocketRepository.UXRocketRepositoryImpl
import com.napoleonit.uxrocket.data.repository.paramsRepository.ParamsRepositoryImpl
import com.napoleonit.uxrocket.data.repository.paramsRepository.IParamsRepository
import com.napoleonit.uxrocket.data.sessionCaching.IMetaInfo
import com.napoleonit.uxrocket.data.sessionCaching.MetaInfo
import com.napoleonit.uxrocket.data.useCases.SaveAppParamsUseCase
import com.napoleonit.uxrocket.data.useCases.CachingParamsUseCase
import com.napoleonit.uxrocket.data.useCases.GetParamsUseCase
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
private const val BASE_URL = "https://apidev.uxrocket.ru/"

@ExperimentalSerializationApi
fun getDataModule(appContext: Context, authKey: String, appRocketId: String) = module {
    /**Base component's*/
    single { provideJson() }

    single { provideDataBase(appContext) }

    single { provideCachingParams() }

    single { provideMetaInfo(appContext, authKey, appRocketId) }

    single { provideInterceptor() }

    single { provideOkHttp(get<Interceptor>()) }

    single { provideRetrofit(get<OkHttpClient>(), get<Json>()) }

    /**Dao*/
    single<UXRocketDao> { get<UXRocketDataBase>().crmDao() }

    /**Api's*/
    single<UXRocketApi> { get<Retrofit>().create(UXRocketApi::class.java) }

    /**Repository's*/
    single<IUXRocketRepository> { UXRocketRepositoryImpl(get<UXRocketApi>()) }

    /**Use case's*/
    single<SaveAppParamsUseCase> { SaveAppParamsUseCase(get<IUXRocketRepository>(), get<IMetaInfo>()) }
    single<CachingParamsUseCase> { CachingParamsUseCase(get<IParamsRepository>()) }
    single<GetParamsUseCase> { GetParamsUseCase(get<IParamsRepository>()) }
}

fun provideCachingParams(): IParamsRepository =
    ParamsRepositoryImpl()

fun provideMetaInfo(appContext: Context, authKey: String, appRocketId: String): IMetaInfo = MetaInfo(appContext, authKey, appRocketId)

@ExperimentalSerializationApi
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