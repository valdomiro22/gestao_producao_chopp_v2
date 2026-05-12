package com.santos.valdomiro.gestaoproducaochopp.di

import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.dao.BarrilDao
import com.santos.valdomiro.gestaoproducaochopp.features.database.ProducaoDatabase
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    fun provideBarrilDao(
        database: ProducaoDatabase
    ): BarrilDao {
        return database.barrilDao()
    }

}