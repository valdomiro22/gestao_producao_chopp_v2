package com.santos.valdomiro.gestaoproducaochopp.common.exceptions

sealed class AppDataException(message: String, cause: Throwable? = null) : Exception(message, cause)

class AcessoNegadoException(causa: Throwable? = null) : AppDataException("Sem permissão de acesso", causa)
class NaoEncontradoException(causa: Throwable? = null) : AppDataException("Registro não encontrado", causa)
class ServicoIndisponivelException(causa: Throwable? = null) : AppDataException("Serviço/Internet indisponível", causa)
class ErroRemoteDBException(causa: Throwable? = null) : AppDataException("Erro desconhecido", causa)