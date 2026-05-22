package com.santos.valdomiro.gestaoproducaochopp.di

import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.barrildao.BarrilDao
import com.santos.valdomiro.gestaoproducaochopp.features.database.ProducaoDatabase
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.gradedao.GradeDao
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.movimentacao.MovimentacaoDao
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.producaodao.ProducaoDao
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.produtodao.ProdutoDao
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

    @Provides
    @Singleton
    fun provideProdutoDao(
        database: ProducaoDatabase
    ): ProdutoDao {
        return database.produtoDao()
    }

    @Provides
    @Singleton
    fun provideGradeDao(
        database: ProducaoDatabase
    ): GradeDao {
        return database.gradeDao()
    }

    @Provides
    @Singleton
    fun provideProducaoDao(
        database: ProducaoDatabase
    ): ProducaoDao {
        return database.producaoDao()
    }

    @Provides
    @Singleton
    fun provideMovProducaoDao(
        database: ProducaoDatabase
    ): MovimentacaoDao {
        return database.movProducaoDao()
    }

}