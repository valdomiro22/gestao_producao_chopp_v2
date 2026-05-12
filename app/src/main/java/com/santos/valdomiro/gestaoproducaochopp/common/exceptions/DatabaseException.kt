package com.santos.valdomiro.gestaoproducaochopp.common.exceptions

class RegistroDuplicadoException(
    cause: Throwable? = null
) : Exception("Registro já existe no banco de dados.", cause)

class RegistroInvalidoException(
    cause: Throwable? = null
) : Exception("Registro inválido.", cause)

class ErroBancoDadosDesconhecidoException(
    cause: Throwable? = null
) : Exception("Erro desconhecido no banco de dados.", cause)