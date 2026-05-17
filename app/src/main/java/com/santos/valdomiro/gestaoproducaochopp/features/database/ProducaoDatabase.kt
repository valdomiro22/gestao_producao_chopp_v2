package com.santos.valdomiro.gestaoproducaochopp.features.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.barrildao.BarrilDao
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model.BarrilLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.gradedao.GradeDao
import com.santos.valdomiro.gestaoproducaochopp.features.grade.data.model.GradeLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.model.ProducaoLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.producaodao.ProducaoDao
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.model.ProdutoLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.produtodao.ProdutoDao

@Database(
    entities = [
        BarrilLocalModel::class,
        ProdutoLocalModel::class,
        GradeLocalModel::class,
        ProducaoLocalModel::class
    ],
    version = 1,
    exportSchema = true
)
abstract class ProducaoDatabase : RoomDatabase() {
    abstract fun barrilDao(): BarrilDao
    abstract fun produtoDao(): ProdutoDao
    abstract fun gradeDao(): GradeDao
    abstract fun producaoDao(): ProducaoDao
}