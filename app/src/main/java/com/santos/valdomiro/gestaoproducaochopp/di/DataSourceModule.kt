package com.santos.valdomiro.gestaoproducaochopp.di

import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.localdatasource.BarrilLocalDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.localdatasource.BarrilLocalDataSourceImpl
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.remotedatasource.BarrilRemoteDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.remotedatasource.BarrilRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

//    @Binds
//    @Singleton
//    abstract fun bindAuthDatasource(impl: AuthDataSourceImpl): AuthDataSource
//
//    @Binds
//    @Singleton
//    abstract fun bindStorageDataSource(impl: StorageDataSourceImpl): StorageDataSource
//
//    @Binds
//    @Singleton
//    abstract fun bindUsuarioRemoteDataSource(impl: UsuarioRemoteDataSourceImpl): UsuarioRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindBarrilRemoteDataSource(impl: BarrilRemoteDataSourceImpl): BarrilRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindBarrilLocalDataSource(impl: BarrilLocalDataSourceImpl): BarrilLocalDataSource

//    @Binds
//    @Singleton
//    abstract fun bindProdutoRemoteDataSource(impl: ProdutoRemoteDataSourceImpl): ProdutoRemoteDataSource
//
//    @Binds
//    @Singleton
//    abstract fun bindGradeRemoteDataSource(impl: GradeRemoteDataSourceImpl): GradeRemoteDataSource
//
//    @Binds
//    @Singleton
//    abstract fun bindProducaoRemoteDataSource(impl: ProducaoRemoteDatasourceImpl): ProducaoRemoteDatasource
//
//    @Binds
//    @Singleton
//    abstract fun bindQtHorariaRemoteDataSource(impl: QuantidadeHorariaRemoteDatasourceImpl): QuantidadeHorariaRemoteDatasource

}