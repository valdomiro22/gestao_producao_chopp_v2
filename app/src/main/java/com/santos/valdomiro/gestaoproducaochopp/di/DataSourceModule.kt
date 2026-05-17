package com.santos.valdomiro.gestaoproducaochopp.di

import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.localdatasource.BarrilLocalDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.localdatasource.BarrilLocalDataSourceImpl
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.remotedatasource.BarrilRemoteDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.remotedatasource.BarrilRemoteDataSourceImpl
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.localdatasource.GradeLocalDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.localdatasource.GradeLocalDataSourceImpl
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.remotedatasource.GradeRemoteDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.remotedatasource.GradeRemoteDataSourceImpl
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.localdatasource.ProducaoLocalDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.localdatasource.ProducaoLocalDataSourceImpl
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.remotedatasource.ProducaoRemoteDatasource
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.remotedatasource.ProducaoRemoteDatasourceImpl
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.localdatasource.ProdutoLocalDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.localdatasource.ProdutoLocalDataSourceImpl
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.remotedatasource.ProdutoRemoteDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.remotedatasource.ProdutoRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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

    // Barril
    @Binds
    @Singleton
    abstract fun bindBarrilRemoteDataSource(impl: BarrilRemoteDataSourceImpl): BarrilRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindBarrilLocalDataSource(impl: BarrilLocalDataSourceImpl): BarrilLocalDataSource

    // Produto
    @Binds
    @Singleton
    abstract fun bindProdutoRemoteDataSource(impl: ProdutoRemoteDataSourceImpl): ProdutoRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindProdutoLocalDataSource(impl: ProdutoLocalDataSourceImpl): ProdutoLocalDataSource

    // Grade
    @Binds
    @Singleton
    abstract fun bindGradeRemoteDataSource(impl: GradeRemoteDataSourceImpl): GradeRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindGradeLocalDataSource(impl: GradeLocalDataSourceImpl): GradeLocalDataSource

    // Produção
    @Binds
    @Singleton
    abstract fun bindProducaoRemoteDataSource(impl: ProducaoRemoteDatasourceImpl): ProducaoRemoteDatasource

    @Binds
    @Singleton
    abstract fun bindProducaoLocalDataSource(impl: ProducaoLocalDataSourceImpl): ProducaoLocalDataSource

//    @Binds
//    @Singleton
//    abstract fun bindQtHorariaRemoteDataSource(impl: QuantidadeHorariaRemoteDatasourceImpl): QuantidadeHorariaRemoteDatasource

}