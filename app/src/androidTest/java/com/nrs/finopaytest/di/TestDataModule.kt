package com.nrs.finopaytest.di

import com.nrs.finopaytest.data.di.DataModule
import com.nrs.finopaytest.domain.repository.WeatherRepository
import com.nrs.finopaytest.repository.FakeWeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
abstract class TestDataModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        fakeWeatherRepository: FakeWeatherRepository
    ): WeatherRepository
}
