package com.santos.valdomiro.gestaoproducaochopp.features.producao.data.localdatasource

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.ErroBancoDadosDesconhecidoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.RegistroDuplicadoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.RegistroInvalidoException
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.model.ProducaoLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.producao.data.producaodao.ProducaoDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class ProducaoLocalDataSourceImpl @Inject constructor(
    private val producaoDao: ProducaoDao
) : ProducaoLocalDataSource {

    override suspend fun insertProducao(producao: ProducaoLocalModel) {
        mapearExceptions { producaoDao.insert(producao = producao) }
    }

    override suspend fun updateProducao(producao: ProducaoLocalModel) {
        mapearExceptions { producaoDao.update(producao = producao) }
    }

    override suspend fun updateStatusSincronizacao(
        producaoId: String,
        statusSincronizacao: StatusSincronizacao
    ) {
        mapearExceptions {
            val linhasAfetadas = producaoDao.updateStatusSincronizacao(
                id = producaoId,
                statusSincronizacao = statusSincronizacao.name
            )

            if (linhasAfetadas == 0) {
                throw RegistroInvalidoException(
                    IllegalStateException("Produção não encontrada para atualizar status.")
                )
            }
        }
    }

    override suspend fun deleteProducao(producao: ProducaoLocalModel) {
        mapearExceptions { producaoDao.delete(producao = producao) }
    }

    override fun getOneById(producaoId: String): Flow<ProducaoLocalModel?> {
        return producaoDao.getOneById(producaoId = producaoId)
            .catch { e ->
                throw mapearException(e)
            }
    }

    override fun getAllProducoes(): Flow<List<ProducaoLocalModel>> {
        return producaoDao.getAll()
            .catch { e ->
                throw mapearException(e)
            }
    }

    override fun getAllProducoesDaGrade(gradeId: String): Flow<List<ProducaoLocalModel>> {
        return producaoDao.getAllDaGrade(gradeId = gradeId)
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