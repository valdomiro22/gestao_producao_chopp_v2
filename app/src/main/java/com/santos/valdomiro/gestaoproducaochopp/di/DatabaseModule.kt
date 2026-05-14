package com.santos.valdomiro.gestaoproducaochopp.di

import android.content.Context
import androidx.room.Room
import com.santos.valdomiro.gestaoproducaochopp.features.database.ProducaoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): ProducaoDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = ProducaoDatabase::class.java,
            name = "producao_database"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }
}