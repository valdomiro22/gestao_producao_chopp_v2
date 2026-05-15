package com.santos.valdomiro.gestaoproducaochopp.features.barril.data.localdatasource

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.ErroBancoDadosDesconhecidoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.RegistroDuplicadoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.RegistroInvalidoException
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.dao.BarrilDao
import com.santos.valdomiro.gestaoproducaochopp.features.barril.data.model.BarrilLocalModel
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class BarrilLocalDataSourceImpl @Inject constructor(
    private val barrilDao: BarrilDao
) : BarrilLocalDataSource {

    override suspend fun insertBarril(barril: BarrilLocalModel) {
        mapearExceptions { barrilDao.insert(barril = barril) }
    }

    override suspend fun updateBarril(barril: BarrilLocalModel) {
        mapearExceptions { barrilDao.update(barril = barril) }
    }

    override suspend fun updateStatusSincronizacao(
        barrilId: String,
        statusSincronizacao: StatusSincronizacao
    ) {
        mapearExceptions {
            val linhasAfetadas = barrilDao.updateStatusSincronizacao(
                id = barrilId,
                statusSincronizacao = statusSincronizacao.name
            )

            if (linhasAfetadas == 0) {
                throw RegistroInvalidoException(
                    IllegalStateException("Barril não encontrado para atualizar status.")
                )
            }
        }
    }

    override fun getOneById(barrilId: String): Flow<BarrilLocalModel?> {
        return barrilDao.getOneById(id = barrilId)
            .catch { e ->
                throw mapearException(e)
            }
    }

    override suspend fun deleteBarril(barril: BarrilLocalModel) {
        mapearExceptions { barrilDao.delete(barril = barril) }
    }

    override fun getAllBarris(): Flow<List<BarrilLocalModel>> {
        return barrilDao.getAll()
            .catch { e ->
                throw mapearException(e)
            }
    }

    private suspend fun <T> mapearExceptions(action: suspend () -> T): T {
        return try {
            action()
        } catch (e: Exception) {
            throw mapearException(e)
        }
    }

    private fun mapearException(e: Throwable): Exception {
        return when (e) {
            is SQLiteConstraintException -> RegistroDuplicadoException(e)
            is IllegalStateException -> RegistroInvalidoException(e)
            is SQLiteException -> ErroBancoDadosDesconhecidoException(e)
            is Exception -> e
            else -> ErroBancoDadosDesconhecidoException(e)
        }
    }
}