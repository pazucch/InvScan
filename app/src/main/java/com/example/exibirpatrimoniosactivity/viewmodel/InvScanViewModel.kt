package com.example.exibirpatrimoniosactivity.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.exibirpatrimoniosactivity.data.AppDatabase
import com.example.exibirpatrimoniosactivity.data.Configuracoes
import com.example.exibirpatrimoniosactivity.data.Conquista
import com.example.exibirpatrimoniosactivity.data.Local
import com.example.exibirpatrimoniosactivity.data.Patrimonio
import com.example.exibirpatrimoniosactivity.data.PerfilUsuario
import com.example.exibirpatrimoniosactivity.data.TipoPatrimonio
import com.example.exibirpatrimoniosactivity.data.paraTexto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * Patrimônios e locais agora são gravados no banco (Room).
 * As listas continuam sendo mutableStateListOf, então as telas não mudam:
 * a lista em memória é o que a tela mostra e cada alteração também vai para o banco.
 * Perfil e configurações continuam só em memória.
 */
class InvScanViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val patrimonioDAO = db.patrimonioDAO()
    private val localDAO = db.localDAO()

    val patrimonios = mutableStateListOf<Patrimonio>()
    val locais = mutableStateListOf<Local>()

    var perfil by mutableStateOf(PerfilUsuario())
        private set

    var config by mutableStateOf(Configuracoes())
        private set

    private var proximoPatrimonioId = 1
    private var proximoLocalId = 1

    init {
        CoroutineScope(Dispatchers.IO).launch {
            carregarDoBanco()
        }
    }

    // ---------- Banco de dados ----------

    private suspend fun carregarDoBanco() {
        try {
            val locaisBD = localDAO.buscarLocais()
            val patrimoniosBD = patrimonioDAO.buscarPatrimonios()

            locais.clear()
            locais.addAll(locaisBD)
            patrimonios.clear()
            patrimonios.addAll(patrimoniosBD)

            proximoLocalId = (locais.maxOfOrNull { it.id } ?: 0) + 1
            proximoPatrimonioId = (patrimonios.maxOfOrNull { it.id } ?: 0) + 1

            Log.d("Busca Ok", "... ${patrimonios.size} patrimonios, ${locais.size} locais")
        } catch (e: Exception) {
            Log.e("Erro ao buscar", "Msg: ${e.message}")
        }
    }

    private suspend fun inserirPatrimonioBD(p: Patrimonio) {
        try {
            patrimonioDAO.inserir(p)
        } catch (e: Exception) {
            Log.e("erro ao inserir", "Msg ${e.message}")
        }
    }

    private suspend fun atualizarPatrimonioBD(p: Patrimonio) {
        try {
            patrimonioDAO.atualizar(p)
        } catch (e: Exception) {
            Log.e("erro ao atualizar", "Msg ${e.message}")
        }
    }

    private suspend fun deletarPatrimonioBD(p: Patrimonio) {
        try {
            patrimonioDAO.deletar(p)
        } catch (e: Exception) {
            Log.e("erro ao deletar", "Msg ${e.message}")
        }
    }

    private suspend fun inserirLocalBD(l: Local) {
        try {
            localDAO.inserir(l)
        } catch (e: Exception) {
            Log.e("erro ao inserir", "Msg ${e.message}")
        }
    }

    private suspend fun atualizarLocalBD(l: Local) {
        try {
            localDAO.atualizar(l)
        } catch (e: Exception) {
            Log.e("erro ao atualizar", "Msg ${e.message}")
        }
    }

    private suspend fun deletarLocalBD(l: Local) {
        try {
            localDAO.deletar(l)        } catch (e: Exception) {
            Log.e("erro ao deletar", "Msg ${e.message}")
        }
    }

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

    /** Cria o item (no topo da lista), grava no banco e devolve o id dele. */
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
            imagens = imagens.paraTexto()
        )
        patrimonios.add(0, novo)
        CoroutineScope(Dispatchers.IO).launch { inserirPatrimonioBD(novo) }
        return novo.id
    }

    fun atualizarPatrimonio(atualizado: Patrimonio) {
        val indice = patrimonios.indexOfFirst { it.id == atualizado.id }
        if (indice >= 0) {
            patrimonios[indice] = atualizado
            CoroutineScope(Dispatchers.IO).launch { atualizarPatrimonioBD(atualizado) }
        }
    }

    fun alternarConferido(id: Int) {
        buscarPatrimonio(id)?.let { atualizarPatrimonio(it.copy(conferido = !it.conferido)) }
    }

    fun removerPatrimonio(id: Int) {
        val patrimonio = buscarPatrimonio(id) ?: return
        patrimonios.removeAll { it.id == id }
        CoroutineScope(Dispatchers.IO).launch { deletarPatrimonioBD(patrimonio) }
    }

    // ---------- Locais ----------

    fun buscarLocal(id: Int): Local? = locais.firstOrNull { it.id == id }

    fun nomeDoLocal(id: Int): String = buscarLocal(id)?.nome ?: "Sem local"

    fun patrimoniosDoLocal(localId: Int): List<Patrimonio> = patrimonios.filter { it.localId == localId }

    fun adicionarLocal(nome: String, descricao: String) {
        val novo = Local(proximoLocalId++, nome.trim(), descricao.trim())
        locais.add(novo)
        CoroutineScope(Dispatchers.IO).launch { inserirLocalBD(novo) }
    }

    fun atualizarLocal(atualizado: Local) {
        val indice = locais.indexOfFirst { it.id == atualizado.id }
        if (indice >= 0) {
            locais[indice] = atualizado
            CoroutineScope(Dispatchers.IO).launch { atualizarLocalBD(atualizado) }
        }
    }

    /** Só remove locais sem itens. Devolve false se não puder remover. */
    fun removerLocal(id: Int): Boolean {
        if (patrimonios.any { it.localId == id }) return false
        val local = buscarLocal(id) ?: return false
        locais.removeAll { it.id == id }
        CoroutineScope(Dispatchers.IO).launch { deletarLocalBD(local) }
        return true
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
