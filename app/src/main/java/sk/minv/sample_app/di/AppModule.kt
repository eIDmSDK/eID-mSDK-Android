package sk.minv.sample_app.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sk.minv.base.utils.helpers.AppDispatchers
import sk.minv.sample_app.utils.common.AppConstants
import sk.minv.sample_app.utils.managers.Preferences
import sk.minv.sample_app.utils.managers.PreferencesImpl
import java.util.concurrent.TimeUnit

val appModule = module {

    single<Preferences> { PreferencesImpl(get()) }

    single { AppDispatchers() }

    single { GsonConverterFactory.create() }

    single { HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(AppConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(AppConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("http://localhost/")
            .addConverterFactory(get<GsonConverterFactory>())
            .client(get())
            .build()
    }
}