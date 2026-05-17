package com.santos.valdomiro.gestaoproducaochopp.di

import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.repository.BarrilRepositoryImpl
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository.BarrilRepository
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.repository.GradeRepositoryImpl
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.repository.GradeRepository
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.repository.ProdutoRepositoryImpl
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.repository.ProdutoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

//    @Binds
//    @Singleton
//    abstract fun bindAuthRepository(
//        authRepository: AuthRepositoryImpl
//    ): AuthRepository
//
//    @Binds
//    @Singleton
//    abstract fun bindStorageRepository(
//        storageRepositoryImpl: StorageRepositoryImpl
//    ): StorageRepository
//
//    @Binds
//    @Singleton
//    abstract fun bindUsuarioRepository(
//        usuarioFirestoreRepositoryImpl: UsuarioFirestoreFirestoreRepositoryImpl
//    ): UsuarioFirestoreRepository

    // Barril
    @Binds
    @Singleton
    abstract fun bindBarrilRepository(
        barrilRepositoryImpl: BarrilRepositoryImpl
    ): BarrilRepository

    // Produto
    @Binds
    @Singleton
    abstract fun bindProdutoRepository(
        produtoRepositoryImpl: ProdutoRepositoryImpl
    ): ProdutoRepository

    // Grade
    @Binds
    @Singleton
    abstract fun bindGradeRepository(
        gradeRepositoryImpl: GradeRepositoryImpl
    ): GradeRepository

//    @Binds
//    @Singleton
//    abstract fun bindProducaoRepository(
//        producaoRepositoryImpl: ProducaoRepositoryImpl
//    ): ProducaoRepository
//
//    @Binds
//    @Singleton
//abstract fun binQtHorariaRepository(
//        qtHorariaRepositoryImpl: QuantidadeHorariaRepositoryImpl
//    ): QuantidadeHorariaRepository

}