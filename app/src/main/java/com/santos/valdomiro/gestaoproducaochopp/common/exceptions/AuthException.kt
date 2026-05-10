package com.santos.valdomiro.gestaoproducaochopp.common.exceptions

sealed class AuthException : Exception() {
    data object EmailEmUso : AuthException()
    data object SenhaFraca : AuthException()
    data object EmailInvalido : AuthException()
    data object ErroDeRede : AuthException()
    data object CredenciaisInvalidas : AuthException()
    data object UsuarioNaoEncontrado : AuthException()
    data object ReautenticacaoNecessaria : AuthException()

    data class Desconhecido(val erroOriginal: Throwable?) : AuthException()
}