package com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.localdatasource

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.santos.valdomiro.gestaoproducaochopp.common.enums.StatusSincronizacao
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.ErroBancoDadosDesconhecidoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.RegistroDuplicadoException
import com.santos.valdomiro.gestaoproducaochopp.common.exceptions.RegistroInvalidoException
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.model.MovimentacaoLocalModel
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.data.movimentacaodao.MovimentacaoDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class MovimentacaoLocalDataSourceImpl @Inject constructor(
    private val movimentacaoDao: MovimentacaoDao
) : MovimentacaoLocalDataSource {

    override suspend fun insertMovimentacao(movimentacao: MovimentacaoLocalModel) {
        mapearExceptions { movimentacaoDao.insert(movimentacao = movimentacao) }
    }

    override suspend fun updateMovimentacao(movimentacao: MovimentacaoLocalModel) {
        mapearExceptions { movimentacaoDao.update(movimentacao = movimentacao) }
    }

    override suspend fun updateStatusSincronizacao(
        movimentacaoId: String,
        statusSincronizacao: StatusSincronizacao
    ) {
        val linhasAfetadas = movimentacaoDao.updateStatusSincronizacao(
            movimentacaoId = movimentacaoId,
            statusSincronizacao = statusSincronizacao.name
        )

        if (linhasAfetadas == 0) {
            throw RegistroInvalidoException(
                IllegalStateException("Movimentação da produção não encontrado para atualizar status.")
            )
        }
    }

    override fun getOneById(movimentacaoId: String): Flow<MovimentacaoLocalModel?> {
        return movimentacaoDao.getOneById(movimentacaoId = movimentacaoId)
            .catch { e ->
                throw mapearException(e)
            }
    }

    override suspend fun deleteMovimentacao(movimentacao: MovimentacaoLocalModel) {
        mapearExceptions { movimentacaoDao.delete(movimentacao = movimentacao) }
    }

    override fun getAllMovimentacoes(): Flow<List<MovimentacaoLocalModel>> {
        return movimentacaoDao.getAll()
            .catch { e ->
                throw mapearException(e)
            }
    }

    override fun getAllOfProducao(producaoId: String): Flow<List<MovimentacaoLocalModel>> {
        return movimentacaoDao.getAllOfProducao(producaoId = producaoId)
            .catch { e ->
                throw mapearException(e)
            }
    }

    override fun getAllMovimentacoesDoHorario(
        horarioReferente: String,
        producaoId: String
    ): Flow<List<MovimentacaoLocalModel>> {
        return movimentacaoDao.getAllMovimentacoesOfHorario(
            horarioReferente = horarioReferente,
            producaoId = producaoId
        )
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