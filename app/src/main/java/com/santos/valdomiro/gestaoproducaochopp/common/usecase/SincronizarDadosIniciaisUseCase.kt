package com.santos.valdomiro.gestaoproducaochopp.common.usecase

import com.santos.valdomiro.gestaoproducaochopp.features.barril.domain.repository.BarrilRepository
import com.santos.valdomiro.gestaoproducaochopp.features.grade.domain.repository.GradeRepository
import com.santos.valdomiro.gestaoproducaochopp.features.movimentacao.domain.repository.MovimentacaoRepository
import com.santos.valdomiro.gestaoproducaochopp.features.producao.domain.repository.ProducaoRepository
import com.santos.valdomiro.gestaoproducaochopp.features.produto.domain.repository.ProdutoRepository
import jakarta.inject.Inject

class SincronizarDadosIniciaisUseCase @Inject constructor(
    private val gradeRepository: GradeRepository,
    private val barrilRepository: BarrilRepository,
    private val produtoRepository: ProdutoRepository,
    private val producaoRepository: ProducaoRepository,
    private val movimentacaoRepository: MovimentacaoRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            gradeRepository.sincronizarGradesDoRemoto()
            barrilRepository.sincronizarBarrisDoRemoto()
            produtoRepository.sincronizarProdutosDoRemoto()
            producaoRepository.sincronizarProducoesDoRemoto()
            movimentacaoRepository.sincronizarMovimentacoesDoRemoto()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}