package com.santos.valdomiro.gestaoproducaochopp.features.barril.data.repository

import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.remotedatasource.BarrilRemoteDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.mapper.toDto
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.mapper.toEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.entity.BarrilEntity
import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository.BarrilRepository
import javax.inject.Inject

class BarrilRepositoryImpl @Inject constructor(
    private val barrilRemoteDataSource: BarrilRemoteDataSource
) : BarrilRepository {

    override suspend fun insertBarril(barril: BarrilEntity): Result<Unit> {
        return try {
            val dto = barril.toDto()
            barrilRemoteDataSource.insertBarril(dto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateBarril(id: String, barril: BarrilEntity): Result<Unit> {
        return try {
            val dto = barril.toDto()
            barrilRemoteDataSource.updateBarril(id, dto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBarril(id: String): Result<BarrilEntity?> {
        return try {
            val dto = barrilRemoteDataSource.getBarril(id)
            val entity = dto?.toEntity()
            Result.success(entity)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteBarril(id: String): Result<Unit> {
        return try {
            barrilRemoteDataSource.deleteBarril(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllBarris(): Result<List<BarrilEntity>> {
        return try {
            val dtos = barrilRemoteDataSource.getAllBarris()
            val entities = dtos.map { it.toEntity() }
            Result.success(entities)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}