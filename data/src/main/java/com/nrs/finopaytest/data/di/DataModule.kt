package com.nrs.finopaytest.data.di

import android.content.Context
import androidx.room.Room
import com.nrs.finopaytest.data.local.WeatherDao
import com.nrs.finopaytest.data.local.WeatherDatabase
import com.nrs.finopaytest.data.remote.WeatherApiService
import com.nrs.finopaytest.data.repository.WeatherRepositoryImpl
import com.nrs.finopaytest.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideWeatherApiService(): WeatherApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(WeatherApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(WeatherApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weather_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWeatherDao(db: WeatherDatabase): WeatherDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(
        api: WeatherApiService,
        dao: WeatherDao
    ): WeatherRepository {
        return WeatherRepositoryImpl(api, dao)
    }
}
