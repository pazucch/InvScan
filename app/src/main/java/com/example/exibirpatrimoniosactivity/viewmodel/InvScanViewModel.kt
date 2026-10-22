package com.example.exibirpatrimoniosactivity.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.exibirpatrimoniosactivity.data.Configuracoes
import com.example.exibirpatrimoniosactivity.data.Conquista
import com.example.exibirpatrimoniosactivity.data.DadosIniciais
import com.example.exibirpatrimoniosactivity.data.Local
import com.example.exibirpatrimoniosactivity.data.Patrimonio
import com.example.exibirpatrimoniosactivity.data.PerfilUsuario
import com.example.exibirpatrimoniosactivity.data.TipoPatrimonio
import java.util.Locale

/**
 * Guarda todo o estado do app (só em memória, como pede o Trabalho 2).
 * As listas são mutableStateListOf: qualquer mudança recompõe as telas.
 */
class InvScanViewModel : ViewModel() {

    val patrimonios = mutableStateListOf<Patrimonio>().apply { addAll(DadosIniciais.patrimonios) }
    val locais = mutableStateListOf<Local>().apply { addAll(DadosIniciais.locais) }

    var perfil by mutableStateOf(PerfilUsuario())
        private set

    var config by mutableStateOf(Configuracoes())
        private set

    private var proximoPatrimonioId = (patrimonios.maxOfOrNull { it.id } ?: 0) + 1
    private var proximoLocalId = (locais.maxOfOrNull { it.id } ?: 0) + 1

    // ---------- Patrimônios ----------

    fun buscarPatrimonio(id: Int): Patrimonio? = patrimonios.firstOrNull { it.id == id }

    fun buscarPorCodigo(codigo: String): Patrimonio? =
        patrimonios.firstOrNull { it.codigo.equals(codigo.trim(), ignoreCase = true) }

    fun codigoExiste(codigo: String): Boolean = buscarPorCodigo(codigo) != null

    fun pesquisar(texto: String): List<Patrimonio> {
        val termo = texto.trim()
        if (termo.isEmpty()) return patrimonios.toList()
        return patrimonios.filter {
            it.nome.contains(termo, ignoreCase = true) ||
                it.codigo.contains(termo, ignoreCase = true) ||
                it.serie.contains(termo, ignoreCase = true)
        }
    }

    fun proximoCodigo(): String {
        val maior = patrimonios.mapNotNull { it.codigo.removePrefix("2-").toIntOrNull() }.maxOrNull() ?: 4320
        return String.format(Locale.ROOT, "2-%06d", maior + 1)
    }

    /** Cria o item (no topo da lista) e devolve o id dele. */
    fun adicionarPatrimonio(
        nome: String,
        tipo: TipoPatrimonio,
        descricao: String,
        serie: String,
        codigo: String,
        localId: Int,
        prateleira: String,
        imagens: List<String>
    ): Int {
        val novo = Patrimonio(
            id = proximoPatrimonioId++,
            codigo = codigo.trim().ifEmpty { proximoCodigo() },
            nome = nome.trim(),
            serie = serie.trim(),
            descricao = descricao.trim(),
            tipo = tipo,
            localId = localId,
            prateleira = prateleira.trim(),
            imagens = imagens
        )
        patrimonios.add(0, novo)
        return novo.id
    }

    fun atualizarPatrimonio(atualizado: Patrimonio) {
        val indice = patrimonios.indexOfFirst { it.id == atualizado.id }
        if (indice >= 0) patrimonios[indice] = atualizado
    }

    fun alternarConferido(id: Int) {
        buscarPatrimonio(id)?.let { atualizarPatrimonio(it.copy(conferido = !it.conferido)) }
    }

    fun removerPatrimonio(id: Int) {
        patrimonios.removeAll { it.id == id }
    }

    // ---------- Locais ----------

    fun buscarLocal(id: Int): Local? = locais.firstOrNull { it.id == id }

    fun nomeDoLocal(id: Int): String = buscarLocal(id)?.nome ?: "Sem local"

    fun patrimoniosDoLocal(localId: Int): List<Patrimonio> = patrimonios.filter { it.localId == localId }

    fun adicionarLocal(nome: String, descricao: String) {
        locais.add(Local(proximoLocalId++, nome.trim(), descricao.trim()))
    }

    fun atualizarLocal(atualizado: Local) {
        val indice = locais.indexOfFirst { it.id == atualizado.id }
        if (indice >= 0) locais[indice] = atualizado
    }

    /** Só remove locais sem itens. Devolve false se não puder remover. */
    fun removerLocal(id: Int): Boolean {
        if (patrimonios.any { it.localId == id }) return false
        return locais.removeAll { it.id == id }
    }

    // ---------- Perfil e configurações ----------

    fun atualizarPerfil(novo: PerfilUsuario) {
        perfil = novo
    }

    fun atualizarConfig(nova: Configuracoes) {
        config = nova
    }

    fun conquistas(): List<Conquista> {
        val conferidos = patrimonios.count { it.conferido }
        val tudoConferido = patrimonios.isNotEmpty() && conferidos == patrimonios.size
        return listOf(
            Conquista("Colecionador", "Cadastre 8 ou mais itens (você tem ${patrimonios.size}).", patrimonios.size >= 8),
            Conquista("Conferente", "Confira 3 ou mais itens (você conferiu $conferidos).", conferidos >= 3),
            Conquista("Organizador", "Cadastre 4 ou mais locais (você tem ${locais.size}).", locais.size >= 4),
            Conquista("Inventário completo", "Deixe todos os itens conferidos.", tudoConferido)
        )
    }
}
