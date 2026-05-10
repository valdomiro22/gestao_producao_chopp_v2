package com.santos.valdomiro.gestaoproducaochopp.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthDataSourceModule {

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
//
//    @Binds
//    @Singleton
//    abstract fun bindBarrilRemoteDataSource(impl: BarrilRemoteDataSourceImpl): BarrilRemoteDataSource
//
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