package com.santos.valdomiro.gestaoproducaochopp.features.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.produtodao.BarrilDao
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model.BarrilLocalModel

@Database(
    entities = [
        BarrilLocalModel::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class ProducaoDatabase : RoomDatabase() {
    abstract fun barrilDao(): BarrilDao
}