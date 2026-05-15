package com.santos.valdomiro.gestaoproducaochopp.di

import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.produtodao.BarrilDao
import com.santos.valdomiro.gestaoproducaochopp.features.database.ProducaoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideBarrilDao(
        database: ProducaoDatabase
    ): BarrilDao {
        return database.barrilDao()
    }

}