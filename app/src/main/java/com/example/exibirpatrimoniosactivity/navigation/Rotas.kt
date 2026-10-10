package com.example.exibirpatrimoniosactivity.navigation

/** Todas as rotas do app, como const val String. */
object Rotas {
    const val INICIO = "inicio"
    const val MENU = "menu"
    const val PATRIMONIOS = "patrimonios"
    const val PATRIMONIO_DETALHE = "patrimonio/{id}"
    const val CRIAR_ITEM = "criar_item"
    const val LOCAIS = "locais"
    const val LOCAL_DETALHE = "local/{id}"
    const val ESCANEAR = "escanear"
    const val USUARIO = "usuario"
    const val CONFIGURACOES = "configuracoes"
    const val SUPORTE = "suporte"

    const val ARG_ID = "id"

    fun patrimonioDetalhe(id: Int) = "patrimonio/$id"
    fun localDetalhe(id: Int) = "local/$id"
}
