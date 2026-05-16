package com.santos.valdomiro.gestaoproducaochopp.features.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.produtodao.BarrilDao
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model.BarrilLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.model.ProdutoLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.produto.data.produtodao.ProdutoDao

@Database(
    entities = [
        BarrilLocalModel::class,
        ProdutoLocalModel::class
    ],
    version = 1,
    exportSchema = true
)
abstract class ProducaoDatabase : RoomDatabase() {
    abstract fun barrilDao(): BarrilDao
    abstract fun produtoDao(): ProdutoDao
}